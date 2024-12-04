package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.payload.response.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value="SELECT c FROM Comment c WHERE c.id = :id AND c.censored = false")
    Comment getValidComment(int id);


    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.CommentResponse(" +
                    "c.id, " +
                    "c.user.username, " +
                    "CASE WHEN c.censored = false THEN c.comment ELSE '***' END, " +
                    "CAST(c.createdAt AS localdate)," +
                    "c.parent.id, " +
                    "c.censored) " +
                    "FROM Comment c " +
                    "WHERE c.post.id = :postId " +
                    "ORDER BY c.updatedAt DESC")
    Page<CommentResponse> getComments(int postId, Pageable pageable);
}
