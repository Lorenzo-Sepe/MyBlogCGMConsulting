package it.cgmconsulting.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.cgmconsulting.myblog.service.RatingSevice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RatingController {
    private final RatingSevice ratingService;

    @PostMapping("/v1/ratings/{postId}/{userId}/{rate}")
    public ResponseEntity

}
