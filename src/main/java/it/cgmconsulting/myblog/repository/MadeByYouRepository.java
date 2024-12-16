package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.MadeByYou;
import it.cgmconsulting.myblog.payload.response.MadeByYouResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface MadeByYouRepository extends JpaRepository<MadeByYou, Integer> {

    boolean existsByPostIdAndUserId(int postId, int userId);

    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.MadeByYouResponse(" +
            "m.id, " +
            "(:imagePath || m.image) AS image, " + // || in jpql equals CONCAT
            "m.description, " +
            "m.user.username) " +
            "FROM MadeByYou m " +
            "WHERE m.post.id = :postId AND m.censored = false")
    Page<MadeByYouResponse> getArtifacts(int postId, Pageable pageable, String imagePath);

    @Query(value="SELECT m FROM MadeByYou m where m.id = :id and m.censored = false")
    MadeByYou getValidMadeByYou(int id);
}
