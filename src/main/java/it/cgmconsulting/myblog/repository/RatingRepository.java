package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Rating;
import it.cgmconsulting.myblog.entity.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {

    @Query(value="SELECT COALESCE(AVG(r.rate), 0d) " +
            "FROM Rating r WHERE r.ratingId.post.id = :postId")
    double getPostAverage(int postId);
}
