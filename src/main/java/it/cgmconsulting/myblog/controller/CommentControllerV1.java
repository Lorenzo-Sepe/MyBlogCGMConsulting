package it.cgmconsulting.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.cgmconsulting.myblog.payload.request.CommentRequest;
import it.cgmconsulting.myblog.payload.request.CommentUpdateRequest;
import it.cgmconsulting.myblog.payload.response.CommentResponse;
import it.cgmconsulting.myblog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(
            summary = "GET COMMENTS",
            description = "Method that returns the post comments",
            tags = {"Comment"})
    @GetMapping("/v0/comments/{postId}")
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable int postId,
            @RequestParam(defaultValue = "0") int pageNumber, // numero di pagina da cui partire; 0 è la prima pagina
            @RequestParam(defaultValue = "3") int pageSize, // numero di elementi per pagina
            @RequestParam(defaultValue = "updatedAt") String sortBy, // la colonna presa in considerazione per l'ordinamento
            @RequestParam(defaultValue = "DESC") String direction // ASC o DESC, ordinamento ascendente o discendente
    ){
        return ResponseEntity.ok(commentService.getComments(postId, pageNumber, pageSize, sortBy, direction));
    }

    @Operation(
            summary = "UPDATE COMMENT",
            description = "Il commento può essere modificato se:\n" +
                    "   1) l'autore del commento è lo stesso che lo vuole modificare;\n" +
                    "   2) se il commento fosse presente nella tabella report \n" +
                    "      con lo status di CLOSED_WITHOUT_BAN;",
            tags = {"Comment"})
    @PatchMapping("/v1/comments/{id}")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<CommentResponse> updateComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CommentUpdateRequest request,
            @PathVariable int id){
      return ResponseEntity.ok(commentService.updateComment(userDetails, request, id));
    }
}
