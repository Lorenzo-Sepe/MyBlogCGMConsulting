package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.payload.response.PostResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

    boolean existsByTitle(String title);
    boolean existsByTitleAndIdNot(String title, int id);

    // Le due query qui sotto sono equivalenti
    Optional<Post> findByIdAndPublishedAtIsNotNullAndPublishedAtLessThanEqual(int id, LocalDate now);
    @Query(value="SELECT p FROM Post p " +
            "WHERE p.id = :id " +
            "AND (p.publishedAt IS NOT NULL AND p.publishedAt <= :now)")
    Optional<Post> getPost(int id, LocalDate now);

    // Projection
    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.PostResponse(" +
            "p.id, " +
            "p.title, " +
            "p.overview, " +
            "p.content, " +
            "p.user.username, " +
            "p.image," +
            "p.publishedAt" +
            ") FROM Post p " + // Post scritto esattamente come il nome della classe, con la P maiuscola
            "WHERE p.id = :id " +
            "AND (p.publishedAt IS NOT NULL AND p.publishedAt <= :now)")
    Optional<PostResponse> getPostResponse(int id, LocalDate now);

    List<Post> findByUserId(int authorId);

    @Query(value="SELECT p.id FROM Post p WHERE p.user.id = :authorId")
    List<Integer> getPostByAuthor(int authorId);

    @Modifying
    @Transactional
    @Query(value="UPDATE Post p SET p.user.id = :newAuthorId " +
            "WHERE p.user.id = :oldAuthorId")
    void updatePostsAuthor(int oldAuthorId, int newAuthorId);

    @Modifying
    @Transactional
    @Query(value="UPDATE Post p SET p.user.id = :newAuthorId " +
            "WHERE p.id = :postId")
    void updatePostAuthor(int newAuthorId, int postId);
}
