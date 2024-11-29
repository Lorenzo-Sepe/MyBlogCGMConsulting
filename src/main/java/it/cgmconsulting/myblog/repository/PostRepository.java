package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.payload.response.PostBoxResponse;
import it.cgmconsulting.myblog.payload.response.PostResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            //":imagePath || p.image as image," +
            "CASE WHEN p.image IS NOT NULL THEN :imagePath || p.image ELSE p.image END, " +
            "p.publishedAt" +
            ") FROM Post p " + // Post scritto esattamente come il nome della classe, con la P maiuscola
            "WHERE p.id = :id " +
            "AND (p.publishedAt IS NOT NULL AND p.publishedAt <= :now)")
    Optional<PostResponse> getPostResponse(int id, LocalDate now, String imagePath);

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


    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.PostBoxResponse(" +
            "p.id, " +
            "p.title, " +
            "p.overview, " +
            "p.user.username, " +
            "CASE WHEN p.image IS NOT NULL THEN :imagePath || p.image ELSE p.image END, " +
            "p.publishedAt, " +
            //"(select count(c.id) from Comment c where c.post.id = p.id) as comments" +
            "SIZE(p.comments) as comments, " +
            "(SELECT COALESCE(AVG(r.rate), 0d) FROM Rating r WHERE r.ratingId.post.id = p.id) as rating" +
            ") FROM Post p " +
            "INNER JOIN p.tags t ON (t.id = :tag AND t.visible = true) " +
            "WHERE p.publishedAt IS NOT NULL AND p.publishedAt <= :now",
    countQuery = "SELECT COUNT(p.id) FROM Post p " +
            "INNER JOIN p.tags t ON (t.id = :tag AND t.visible = true) " +
            "WHERE p.publishedAt IS NOT NULL AND p.publishedAt <= :now")
    Page<PostBoxResponse> getVisiblePostsByTag(Pageable pageable, String tag, LocalDate now, String imagePath);

    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.PostBoxResponse(" +
            "p.id, " +
            "p.title, " +
            "p.overview, " +
            "p.user.username, " +
            "CASE WHEN p.image IS NOT NULL THEN :imagePath || p.image ELSE p.image END, " +
            "p.publishedAt, " +
            //"(select count(c.id) from Comment c where c.post.id = p.id) as comments" +
            "SIZE(p.comments) as comments, " +
            "(SELECT COALESCE(AVG(r.rate), 0d) FROM Rating r WHERE r.ratingId.post.id = p.id) as rating" +
            ") FROM Post p " +
            "WHERE p.publishedAt IS NOT NULL AND p.publishedAt <= :now",
            countQuery = "SELECT COUNT(p.id) FROM Post p " +
                    "WHERE p.publishedAt IS NOT NULL AND p.publishedAt <= :now")
    Page<PostBoxResponse> getLatestPostHomePage(Pageable pageable, LocalDate now, String imagePath);

    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.PostBoxResponse(" +
            "p.id, " +
            "p.title, " +
            "p.overview, " +
            "p.user.username, " +
            "CASE WHEN p.image IS NOT NULL THEN :imagePath || p.image ELSE p.image END, " +
            "p.publishedAt, " +
            //"(select count(c.id) from Comment c where c.post.id = p.id) as comments" +
            "SIZE(p.comments) as comments, " +
            "(SELECT COALESCE(AVG(r.rate), 0d) FROM Rating r WHERE r.ratingId.post.id = p.id) as rating" +
            ") FROM Post p " +
            "WHERE p.publishedAt IS NOT NULL AND p.publishedAt <= :now " +
            "AND p.user.username = :username",
            countQuery = "SELECT COUNT(p.id) FROM Post p " +
                    "WHERE p.publishedAt IS NOT NULL AND p.publishedAt <= :now " +
                    "AND p.user.username = :username")
    Page<PostBoxResponse> getPaginatedPostsByAuthor(Pageable pageable, String username, LocalDate now, String imagePath);

    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.PostBoxResponse(" +
            "p.id, " +
            "p.title, " +
            "p.overview, " +
            "p.user.username, " +
            "CASE WHEN p.image IS NOT NULL THEN :imagePath || p.image ELSE p.image END, " +
            "p.publishedAt, " +
            //"(select count(c.id) from Comment c where c.post.id = p.id) as comments" +
            "SIZE(p.comments) as comments, " +
            "(SELECT COALESCE(AVG(r.rate), 0d) FROM Rating r WHERE r.ratingId.post.id = p.id) as rating" +
            ") FROM Post p " +
            "WHERE p.publishedAt IS NOT NULL AND p.publishedAt <= :now " +
            "AND p.title LIKE :keyword",
            countQuery = "SELECT COUNT(p.id) FROM Post p " +
                    "WHERE p.publishedAt IS NOT NULL AND p.publishedAt <= :now " +
                    "AND p.title LIKE :keyword") // ... AND title LIKE '%pasta%' ...
    Page<PostBoxResponse> getPaginatedPostsByKeyWord(Pageable pageable, LocalDate now, String imagePath, String keyword);
}
