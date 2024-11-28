package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.entity.enumeration.AuthorityName;
import it.cgmconsulting.myblog.exception.BadRequestException;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.exception.UnauthorizedException;
import it.cgmconsulting.myblog.payload.request.PostRequest;
import it.cgmconsulting.myblog.payload.response.PostResponse;
import it.cgmconsulting.myblog.repository.PostRepository;
import it.cgmconsulting.myblog.repository.UserRepository;
import it.cgmconsulting.myblog.utils.Msg;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final TagService tagService;

    public PostResponse create(UserDetails userDetails, PostRequest request){
        // verifico che non esista già un post con quel titolo. Il titolo del post sul db è UNIQUE
        if(postRepository.existsByTitle(request.getTitle()))
            throw new BadRequestException(Msg.POST_TITLE_IN_USE);

        User user = (User) userDetails;
        Post post = Post.builder()
                .title(request.getTitle())
                .overview(request.getOverview())
                .content(request.getContent())
                .user(user)
                .build();
        postRepository.save(post);
        return PostResponse.fromEntityToDto(post, null);
    }

    @Transactional
    public PostResponse update(UserDetails userDetails, PostRequest request, int id, String imagePath) {
        // verifico che non esista già un post con quel titolo. Il titolo del post sul db è UNIQUE
        if(postRepository.existsByTitleAndIdNot(request.getTitle(), id))
            throw new BadRequestException(Msg.POST_TITLE_IN_USE);
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));
        // verifico che autore del post e autore che lo modifica coincidano
        if(post.getUser().getId() != ((User) userDetails).getId())
            throw new UnauthorizedException(Msg.POST_UNAUTHORIZED_ACCESS);
        post.setTitle(request.getTitle());
        post.setOverview(request.getOverview());
        post.setContent(request.getContent());
        post.setPublishedAt(null);
        postRepository.save(post); // non serve in virtù dell'annotazione @Transactional
        PostResponse postResponse = PostResponse.fromEntityToDto(post, imagePath);
        postResponse.setTags(tagService.getTagsByPost(id));
        return postResponse;
    }

    public PostResponse getPost(int id, String imagePath) {
         PostResponse postResponse = postRepository.getPostResponse(id, LocalDate.now(), imagePath)
                 .orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));
         postResponse.setTags(tagService.getTagsByPost(id));
         return postResponse;
    }

    @Transactional
    public PostResponse publishPost(int id, LocalDate publishedAt, String imagePath) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));
        post.setPublishedAt(publishedAt);
        PostResponse postResponse = PostResponse.fromEntityToDto(post, imagePath);
        postResponse.setTags(tagService.getTagsByPost(id));
        return postResponse;
    }

    public String reassignPost(int oldAuthorId, int newAuthorId, Optional<Integer> postId) {

        int newAuthor = userRepository.getValidAuthor(AuthorityName.AUTHOR, newAuthorId)
                .orElseThrow(()-> new ResourceNotFoundException("User", "id", newAuthorId));

        if(postId.isEmpty()) {
            // cambio il vecchio author col nuovo su tutti i post
            postRepository.updatePostsAuthor(oldAuthorId, newAuthor);
            return Msg.POSTS_REASSIGNEMENT;
        } else
            // cambio il vecchio author col nuovo solo sul post di cui ho l'id
            postRepository.updatePostAuthor(newAuthor, postId.get());
        return Msg.POST_REASSIGNEMENT;
    }

    @Transactional
    public PostResponse postTags(UserDetails userDetails, int postId, Set<String> tags, String imagePath){
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));
        imageService.checkAuthor(post.getUser().getId(), ((User) userDetails).getId());
        Set<Tag> newTags = tagService.findVisibleTags(tags);
        post.setTags(newTags);
        PostResponse postResponse = PostResponse.fromEntityToDto(post, imagePath);
        postResponse.setTags(tagService.getTagsByPost(postId));
        return postResponse;
    }



}
