package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value="SELECT c FROM Comment c WHERE c.id = :id AND c.censored = false")
    Comment getValidComment(int id);
}
