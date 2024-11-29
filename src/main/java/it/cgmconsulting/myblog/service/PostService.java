package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.entity.enumeration.AuthorityName;
import it.cgmconsulting.myblog.exception.BadRequestException;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.exception.UnauthorizedException;
import it.cgmconsulting.myblog.payload.request.PostRequest;
import it.cgmconsulting.myblog.payload.response.PostBoxResponse;
import it.cgmconsulting.myblog.payload.response.PostResponse;
import it.cgmconsulting.myblog.repository.PostRepository;
import it.cgmconsulting.myblog.repository.UserRepository;
import it.cgmconsulting.myblog.utils.Msg;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final TagService tagService;
    private final UserService userService;

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


    public List<PostBoxResponse> getPaginatedPostsByTag(String tag, int pageNumber, int pageSize, String sortBy, String direction, String imagePath) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.valueOf(direction.toUpperCase()), sortBy);
        Page<PostBoxResponse> list = postRepository.getVisiblePostsByTag(pageable, tag, LocalDate.now(), imagePath);
        if(list.hasContent())
            return list.getContent();
        else return new ArrayList<PostBoxResponse>();
    }

    public List<PostBoxResponse> getPaginatedPostsByAuthor(String username, int pageNumber, int pageSize, String sortBy, String direction, String imagePath) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.valueOf(direction.toUpperCase()), sortBy);
        Page<PostBoxResponse> list = postRepository.getPaginatedPostsByAuthor(pageable, username, LocalDate.now(), imagePath);
        if(list.hasContent())
            return list.getContent();
        else return new ArrayList<PostBoxResponse>();
    }

    public List<PostBoxResponse> getLatestPostHomePage(int pageNumber, int pageSize, String sortBy, String direction, String imagePath) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.valueOf(direction.toUpperCase()), sortBy);
        Page<PostBoxResponse> list = postRepository.getLatestPostHomePage(pageable, LocalDate.now(), imagePath);
        if(list.hasContent())
            return list.getContent();
        else return new ArrayList<PostBoxResponse>();
    }

    public List<PostBoxResponse> getPaginatedPostsByKeyWord(String keyword, int pageNumber, int pageSize, String sortBy, String direction, String imagePath, boolean isExactMatch, boolean isCaseSensitive) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.valueOf(direction.toUpperCase()), sortBy);
        Page<PostBoxResponse> list = postRepository.getPaginatedPostsByKeyWord(pageable, LocalDate.now(), imagePath, '%'+keyword+'%');

        if(!list.getContent().isEmpty()) {

            Pattern pattern = null;

            if (!isCaseSensitive && !isExactMatch)
                pattern = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
            else if (!isCaseSensitive && isExactMatch)
                pattern = Pattern.compile("\\b" + keyword + "\\b", Pattern.CASE_INSENSITIVE);
            else if (isCaseSensitive && !isExactMatch)
                pattern = Pattern.compile(keyword);
            else
                pattern = Pattern.compile("\\b" + keyword + "\\b");

            List<PostBoxResponse> finalList = new ArrayList<>();
            for (PostBoxResponse p : list.getContent()) {
                if (pattern.matcher(p.getTitle()).find())
                    finalList.add(p);
            }
            return finalList;
        }
        return new ArrayList<PostBoxResponse>();
    }

    @Transactional
    public String addPreferredPost(UserDetails userDetails, int postId) {
        Post post = postRepository.findByIdAndPublishedAtIsNotNullAndPublishedAtLessThanEqual(postId, LocalDate.now())
                .orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));
        User user = userService.getUserPreferredPost(((User) userDetails).getId());
        if(user.getPreferredPosts().contains(post)) {
            user.getPreferredPosts().remove(post);
            return Msg.BOOKMARK_REMOVE;
        } else {
            user.getPreferredPosts().add(post);
            return Msg.BOOKMARK_ADD;
        }
    }
}
