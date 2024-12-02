package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.exception.ConflictException;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.request.CommentRequest;
import it.cgmconsulting.myblog.payload.response.CommentResponse;
import it.cgmconsulting.myblog.repository.CommentRepository;
import it.cgmconsulting.myblog.repository.PostRepository;
import it.cgmconsulting.myblog.utils.Msg;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional(rollbackOn = ConflictException.class)
    public CommentResponse addComment(UserDetails userDetails, CommentRequest request) {
        int postId = request.getPostId();
        Post post = postRepository.findByIdAndPublishedAtIsNotNullAndPublishedAtLessThanEqual(postId, LocalDate.now())
                .orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));
        Comment parent = request.getCommentParent() != null ? commentRepository.getValidComment(request.getCommentParent()) : null;
        Comment comment = Comment.builder()
                .comment(request.getComment())
                .user((User)userDetails)
                .post(post)
                .parent(parent)
                .censored(false)
                .build();
        try {
            commentRepository.save(comment);
            return CommentResponse.fromEntityToDto(comment);
        } catch (Exception e){
            throw new ConflictException(Msg.COMMENT_500);
        }
    }
}
