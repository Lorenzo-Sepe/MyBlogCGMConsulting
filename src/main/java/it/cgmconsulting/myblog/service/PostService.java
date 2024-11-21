package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.payload.request.PostRequest;
import it.cgmconsulting.myblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post create(UserDetails userDetails, PostRequest request){
        User user = (User) userDetails;
        Post post = Post.builder()
                .title(request.getTitle())
                .overview(request.getOverview())
                .content(request.getContent())
                .user(user)
                .build();
        postRepository.save(post);
        return post;
    }

}
