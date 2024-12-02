package it.cgmconsulting.myblog.payload.response;

import it.cgmconsulting.myblog.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class CommentResponse {

    private int id;
    private String username;
    private String comment;
    private LocalDate createdAt;
    private Integer parentId;
    private boolean censored;

    public static CommentResponse fromEntityToDto(Comment comment){
        return new CommentResponse(
                comment.getId(),
                comment.getUser().getUsername(),
                comment.getComment(),
                (comment.getCreatedAt()).toLocalDate(),
                comment.getParent() != null ? comment.getParent().getId() : null,
                comment.isCensored()
        );
    }

}
