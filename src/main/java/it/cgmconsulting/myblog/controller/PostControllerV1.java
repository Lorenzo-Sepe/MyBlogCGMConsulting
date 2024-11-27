package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.payload.request.PostRequest;
import it.cgmconsulting.myblog.payload.response.PostResponse;
import it.cgmconsulting.myblog.service.ImageService;
import it.cgmconsulting.myblog.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Validated
public class PostControllerV1 {

    @Value("${application.image.post.size}")
    private long size;
    @Value("${application.image.post.width}")
    private int width;
    @Value("${application.image.post.height}")
    private int height;
    @Value("${application.image.post.extensions}")
    private String[]  extensions;
    @Value("${application.image.post.imagePath}")
    private String  imagePath;

    private final PostService postService;
    private final ImageService imageService;

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
        return ResponseEntity.ok(postService.update(userDetails, request, id, imagePath));
    }

    @GetMapping("/v0/posts/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable int id){
        return ResponseEntity.ok(postService.getPost(id, imagePath));
    }

    @PatchMapping("/v1/posts/{id}/publish")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PostResponse> publishPost(@PathVariable int id, @RequestParam @NotNull @FutureOrPresent LocalDate publishedAt){
        return ResponseEntity.ok(postService.publishPost(id, publishedAt, imagePath));
    }


    @PatchMapping("/v1/posts/massive_reassign/{oldAuthorId}/{newAuthorId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> massiveReassignPost(
            @PathVariable int oldAuthorId,
            @PathVariable int newAuthorId,
            @RequestParam Optional<Integer> postId){
        return ResponseEntity.ok(postService.reassignPost(oldAuthorId, newAuthorId, postId));
    }

    @PatchMapping("/v1/posts/{postId}/image")
    @PreAuthorize("hasAuthority('AUTHOR')")
    public ResponseEntity<PostResponse> addPostImage(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestParam MultipartFile file,
                                                     @PathVariable int postId) throws IOException {
        return ResponseEntity.ok(imageService.addPostImage(userDetails, file, size, width, height, extensions, imagePath, postId));
    }

    @DeleteMapping("/v1/posts/{postId}/image")
    @PreAuthorize("hasAuthority('AUTHOR')")
    public ResponseEntity<PostResponse> deletePostImage(@AuthenticationPrincipal UserDetails userDetails,
                                                        @PathVariable int postId){
        return ResponseEntity.ok(imageService.deletePostImage(userDetails, postId, imagePath));
    }

    @PutMapping("/v1/posts/{postId}/tags")
    @PreAuthorize("hasAuthority('AUTHOR')")
    public ResponseEntity<PostResponse> postTag(@AuthenticationPrincipal UserDetails userDetails,
                                                @PathVariable int postId,
                                                @RequestParam @NotEmpty Set<String> tags){
        return ResponseEntity.ok(postService.postTags(userDetails, postId, tags, imagePath));
    }

}
