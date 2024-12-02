package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.Rating;
import it.cgmconsulting.myblog.entity.RatingId;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.repository.PostRepository;
import it.cgmconsulting.myblog.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final PostRepository postRepository;

    public Double addRateAndReturnNewAverage(UserDetails userDetails, int postId, byte rate) {
        Post post = postRepository.findByIdAndPublishedAtIsNotNullAndPublishedAtLessThanEqual(postId, LocalDate.now())
                .orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));
        Rating rating = new Rating(
                new RatingId((User) userDetails, post),
                rate
                );
        ratingRepository.save(rating);
        return Math.round(ratingRepository.getPostAverage(postId) * 10.0 )/ 10.0; // 4.6667
    }
}
