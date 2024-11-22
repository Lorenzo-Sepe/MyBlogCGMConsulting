package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.payload.request.PostRequest;
import it.cgmconsulting.myblog.payload.response.PostResponse;
import it.cgmconsulting.myblog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostControllerV1 {

    private final PostService postService;

    @PostMapping("/v1/posts")
    @PreAuthorize("hasAuthority('AUTHOR')")
    public ResponseEntity<PostResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid PostRequest request){
        return new ResponseEntity<>(postService.create(userDetails, request), HttpStatus.CREATED);
    }

    @PatchMapping("/v1/posts/{id}")
    @PreAuthorize("hasAuthority('AUTHOR')")
    public ResponseEntity<PostResponse> update(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid PostRequest request,
            @PathVariable int id){
        return ResponseEntity.ok(postService.update(userDetails, request, id));
    }

    @GetMapping("/v0/posts/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable int id){
        return ResponseEntity.ok(postService.getPost(id));
    }

}
