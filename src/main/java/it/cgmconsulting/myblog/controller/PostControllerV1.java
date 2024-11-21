package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.payload.request.PostRequest;
import it.cgmconsulting.myblog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostControllerV1 {

    private final PostService postService;

    @PostMapping("/v1/posts")
    @PreAuthorize("hasAuthority('AUTHOR')")
    public ResponseEntity<Post> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid PostRequest request){
        return ResponseEntity.ok(postService.create(userDetails, request));
    }
}
