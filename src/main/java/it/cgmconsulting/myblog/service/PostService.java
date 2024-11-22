package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.exception.BadRequestException;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.exception.UnauthorizedException;
import it.cgmconsulting.myblog.payload.request.PostRequest;
import it.cgmconsulting.myblog.payload.response.PostResponse;
import it.cgmconsulting.myblog.repository.PostRepository;
import it.cgmconsulting.myblog.utils.Msg;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

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
        return PostResponse.fromEntityToDto(post);
    }

    @Transactional
    public PostResponse update(UserDetails userDetails, PostRequest request, int id) {
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
        return PostResponse.fromEntityToDto(post);
    }

    public PostResponse getPost(int id) {
        /*
        Post post = postRepository.findByIdAndPublishedAtIsNotNullAndPublishedAtLessThanEqual(id, LocalDate.now())
                .orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));
         */
        /*
        Post post = postRepository.getPost(id, LocalDate.now())
                .orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));
        return PostResponse.fromEntityToDto(post);
        */
         PostResponse postResponse = postRepository.getPostResponse(id, LocalDate.now())
                 .orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));
         return postResponse;
    }
}
