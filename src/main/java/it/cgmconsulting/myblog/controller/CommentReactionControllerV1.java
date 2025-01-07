package it.cgmconsulting.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.cgmconsulting.myblog.payload.response.CommentReactionResponse;
import it.cgmconsulting.myblog.payload.response.CommentReactionResponseInterface;
import it.cgmconsulting.myblog.service.CommentReactionService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentReactionControllerV1 {

    private final CommentReactionService commentReactionService;

    // Chiamate lentissime su Swagger a causa della renderizzazione
    // Su postman sono molto performanti (un pelo più lenta quella in sql nativo)

    @Operation(
            summary = "GET COMMENT REACTIONS (Native SQL)",
            description = "Method that returns the comment reactions (Using native sql and mapping the result on a interface)",
            tags = {"CommentReaction"})
    @GetMapping("/v0/commentReactions")
    public ResponseEntity<List<CommentReactionResponseInterface>> getReactions(){
        return ResponseEntity.ok(commentReactionService.getReactions());
    }

    @Operation(
            summary = "GET COMMENT REACTIONS (JPQL)",
            description = "Method that returns the comment reactions (Using @Immutable entity on a logical view and JPQL)",
            tags = {"CommentReaction"})
    @GetMapping("/v0/commentReactions_")
    public ResponseEntity<List<CommentReactionResponse>> getReactions_(){
        return ResponseEntity.ok(commentReactionService.getReactions_());
    }

    @Operation(
            summary = "ADD COMMENT REACTION",
            description = "Method that permits to 'member' to add a reaction",
            tags = {"CommentReaction"})
    @PostMapping("/v1/commentReactions/{commentId}")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<List<CommentReactionResponse>> addReaction(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @NotBlank @Size(max = 8) String reactionName,
            @PathVariable int commentId){
        return ResponseEntity.ok(commentReactionService.addReaction(userDetails, reactionName, commentId));
    }

    @Operation(
            summary = "GET COMMENT REACTION DETAILS",
            description = "Method that returns the users by comment reactions",
            tags = {"CommentReaction"})
    @GetMapping("/v0/usersByReaction/{commentId}")
    public ResponseEntity<List<String>> getUsersByReaction(
            @PathVariable int commentId,
            @RequestParam @NotBlank @Size(max = 8) String reactionName){
        return ResponseEntity.ok(commentReactionService.getUsersByReaction(commentId, reactionName));
    }
}
