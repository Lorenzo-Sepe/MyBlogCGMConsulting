package it.cgmconsulting.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.cgmconsulting.myblog.payload.request.CommentRequest;
import it.cgmconsulting.myblog.payload.response.CommentResponse;
import it.cgmconsulting.myblog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentControllerV1 {

    private final CommentService commentService;

    @Operation(
            summary = "ADD COMMENT",
            description = "Method to create a comment by logged user as member",
            tags = {"Comment"})
    @PostMapping("/v1/comments")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<CommentResponse> addComment(@RequestBody @Valid CommentRequest request,
                                                      @AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<CommentResponse>(commentService.addComment(userDetails, request), HttpStatus.CREATED);
    }
}
