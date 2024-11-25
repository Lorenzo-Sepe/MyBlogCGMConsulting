package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.payload.request.PostRequest;
import it.cgmconsulting.myblog.payload.response.PostResponse;
import it.cgmconsulting.myblog.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Validated
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

    @PatchMapping("/v1/posts/{id}/publish")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PostResponse> publishPost(@PathVariable int id, @RequestParam @NotNull @FutureOrPresent LocalDate publishedAt){
        return ResponseEntity.ok(postService.publishPost(id, publishedAt));
    }



    @PatchMapping("/v1/posts/massive_reassign/{oldAuthorId}/{newAuthorId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> massiveReassignPost(
            @PathVariable int oldAuthorId,
            @PathVariable int newAuthorId,
            @RequestParam Optional<Integer> postId){
        return ResponseEntity.ok(postService.reassignPost(oldAuthorId, newAuthorId, postId));
    }


}
