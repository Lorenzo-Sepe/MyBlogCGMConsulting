package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {

    boolean existsByTitle(String title);
    boolean existsByTitleAndIdNot(String title, int id);
}
