package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.entity.enumeration.ReportStatus;
import it.cgmconsulting.myblog.exception.ConflictException;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.request.CommentRequest;
import it.cgmconsulting.myblog.payload.request.CommentUpdateRequest;
import it.cgmconsulting.myblog.payload.response.CommentResponse;
import it.cgmconsulting.myblog.repository.CommentRepository;
import it.cgmconsulting.myblog.repository.PostRepository;
import it.cgmconsulting.myblog.repository.ReportRepository;
import it.cgmconsulting.myblog.utils.Msg;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ReportRepository reportRepository;

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

    public List<CommentResponse> getComments(int postId, int pageNumber, int pageSize, String sortBy, String direction ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.valueOf(direction.toUpperCase()), sortBy);
        return commentRepository.getComments(postId, pageable).getContent();
    }

    @Transactional
    public CommentResponse updateComment(UserDetails userDetails, CommentUpdateRequest request, int id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Comment", "id", id));
        if(comment.getUser().getId() != ((User) userDetails).getId())
            throw new ConflictException(Msg.COMMENT_UNAUTHORIZED_ACCESS);
        if(comment.isCensored())
            throw new ConflictException(Msg.COMMENT_CENSORED);
        if(reportRepository.existsByCommentAndStatusIsNot(comment, ReportStatus.CLOSED_WITHOUT_BAN))
            throw new ConflictException(Msg.COMMENT_UNDER_CONTROL);

        comment.setComment(request.getComment());
        return CommentResponse.fromEntityToDto(comment);
    }
}
