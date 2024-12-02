package it.cgmconsulting.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.cgmconsulting.myblog.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RatingControllerV1 {

    private final RatingService ratingService;

    @Operation(
            summary = "POST RATING",
            description = "Method to rate the post from logged user as member",
            tags = {"Rating"})
    @PostMapping("/v1/rating/{postId}/{rate}")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<Double> ratePost(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable int postId,
                                           @PathVariable /* @Min(1) @Max(5) in alternativa a @Range */ @Range(min = 1, max = 5) byte rate){
        return ResponseEntity.ok(ratingService.addRateAndReturnNewAverage(userDetails, postId, rate));
    }
}
