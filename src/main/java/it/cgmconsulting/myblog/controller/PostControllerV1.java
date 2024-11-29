package it.cgmconsulting.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.cgmconsulting.myblog.payload.request.PostRequest;
import it.cgmconsulting.myblog.payload.response.PostBoxResponse;
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
import java.util.List;
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
    @Operation(summary = "Create a post", description = "Create a post inside the database", tags = {"Post"})
    public ResponseEntity<PostResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid PostRequest request){
        return new ResponseEntity<>(postService.create(userDetails, request), HttpStatus.CREATED);
    }

    @PatchMapping("/v1/posts/{id}")
    @PreAuthorize("hasAuthority('AUTHOR')")
    @Operation(summary = "Update a post", description = "Edit a post already present in the database", tags = {"Post"})
    public ResponseEntity<PostResponse> update(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid PostRequest request,
            @PathVariable int id){
        return ResponseEntity.ok(postService.update(userDetails, request, id, imagePath));
    }

    @GetMapping("/v0/posts/{id}")
    @Operation(summary = "Fetch a post by id", description = "I fetch a post by its id from the database", tags = {"Post"})
    public ResponseEntity<PostResponse> getPost(@PathVariable int id){
        return ResponseEntity.ok(postService.getPost(id, imagePath));
    }

    @PatchMapping("/v1/posts/{id}/publish")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Publish a post", description = "The admin decides whether to approve the post with the following id", tags = {"Post"})
    public ResponseEntity<PostResponse> publishPost(@PathVariable int id, @RequestParam @NotNull @FutureOrPresent LocalDate publishedAt){
        return ResponseEntity.ok(postService.publishPost(id, publishedAt, imagePath));
    }


    @PatchMapping("/v1/posts/massive_reassign/{oldAuthorId}/{newAuthorId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Reassign all posts", description = "Reassign all of a former employee's posts to a new subordinate", tags = {"Post"})
    public ResponseEntity<String> massiveReassignPost(
            @PathVariable int oldAuthorId,
            @PathVariable int newAuthorId,
            @RequestParam Optional<Integer> postId){
        return ResponseEntity.ok(postService.reassignPost(oldAuthorId, newAuthorId, postId));
    }

    @PatchMapping("/v1/posts/{postId}/image")
    @PreAuthorize("hasAuthority('AUTHOR')")
    @Operation(summary = "Add image to post", description = "Upload an image inside the post", tags = {"Post"})
    public ResponseEntity<PostResponse> addPostImage(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestParam MultipartFile file,
                                                     @PathVariable int postId) throws IOException {
        return ResponseEntity.ok(imageService.addPostImage(userDetails, file, size, width, height, extensions, imagePath, postId));
    }

    @DeleteMapping("/v1/posts/{postId}/image")
    @PreAuthorize("hasAuthority('AUTHOR')")
    @Operation(summary = "Delete an image", description = "Delete an image within a post", tags = {"Post"})
    public ResponseEntity<PostResponse> deletePostImage(@AuthenticationPrincipal UserDetails userDetails,
                                                        @PathVariable int postId){
        return ResponseEntity.ok(imageService.deletePostImage(userDetails, postId, imagePath));
    }

    @PutMapping("/v1/posts/{postId}/tags")
    @PreAuthorize("hasAuthority('AUTHOR')")
    @Operation(summary = "Add tags to the post", description = "Add tags to the post among existing ones", tags = {"Post"})
    public ResponseEntity<PostResponse> postTag(@AuthenticationPrincipal UserDetails userDetails,
                                                @PathVariable int postId,
                                                @RequestParam @NotEmpty Set<String> tags){
        return ResponseEntity.ok(postService.postTags(userDetails, postId, tags, imagePath));
    }

    @GetMapping("/v0/posts/tags")
    @Operation(
            summary = "LIST ALL POST PAGINATED",
            description = "Post pagination",
            tags = {"Post"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "404", description = "Resource not found"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<List<PostBoxResponse>> getPaginatedPostsByTag(
            @RequestParam @NotBlank @Size(max = 30) String tag,
            @RequestParam(defaultValue = "0") int pageNumber, // numero di pagina da cui partire; 0 è la prima pagina
            @RequestParam(defaultValue = "5") int pageSize, // numero di elementi per pagina
            @RequestParam(defaultValue = "publishedAt") String sortBy, // la colonna presa in considerazione per l'ordinamento
            @RequestParam(defaultValue = "DESC") String direction // ASC o DESC, ordinamento ascendente o discendente
    ){
        return ResponseEntity.ok(postService.getPaginatedPostsByTag(tag, pageNumber, pageSize, sortBy, direction, imagePath));
    }

    @Operation(
            summary = "LIST ALL POST PAGINATED",
            description = "Post pagination",
            tags = {"Post"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "404", description = "Resource not found"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/v0/posts/username")
    public ResponseEntity<List<PostBoxResponse>> getPaginatedPostsByAuthor(
            @RequestParam @NotBlank @Size(max = 30) String username,
            @RequestParam(defaultValue = "0") int pageNumber, // numero di pagina da cui partire; 0 è la prima pagina
            @RequestParam(defaultValue = "5") int pageSize, // numero di elementi per pagina
            @RequestParam(defaultValue = "publishedAt") String sortBy, // la colonna presa in considerazione per l'ordinamento
            @RequestParam(defaultValue = "DESC") String direction // ASC o DESC, ordinamento ascendente o discendente
    ){
        return ResponseEntity.ok(postService.getPaginatedPostsByAuthor(username, pageNumber, pageSize, sortBy, direction, imagePath));
    }

    @GetMapping("/v0/posts/keyword") // ricerca dei post per parola chiave nel titolo (exactMatch e caseSensitive)
    @Operation(summary = "Search for post by keyword", description = "Return the post with the specific keyword", tags = {"Post"})
    public ResponseEntity<List<PostBoxResponse>> getPaginatedPostsByKeyWord(
            @RequestParam @NotBlank @Size(max = 30) String keyword,
            @RequestParam(defaultValue = "0") int pageNumber, // numero di pagina da cui partire; 0 è la prima pagina
            @RequestParam(defaultValue = "5") int pageSize, // numero di elementi per pagina
            @RequestParam(defaultValue = "title") String sortBy, // la colonna presa in considerazione per l'ordinamento
            @RequestParam(defaultValue = "ASC") String direction, // ASC o DESC, ordinamento ascendente o discendente
            @RequestParam(defaultValue = "false") boolean isExactMatch,
            @RequestParam(defaultValue = "false") boolean isCaseSensitive
    ){
        /*
        keyword = "pasta"
        exactMatch = true ->  OK: pasta alla norma & .pasta alla gricia & viva la pasta. ; KO: pastasciutta all'olio (la keyword non deve essere contenuta in altra parola)
        exactMatch = false -> OK: pasta alla norma & pastasciutta all'olio
         */
        return ResponseEntity.ok(postService.getPaginatedPostsByKeyWord(keyword, pageNumber, pageSize, sortBy, direction, imagePath, isExactMatch, isCaseSensitive));
    }

    @GetMapping("/v0/posts/home-page") // in realta va bene per paginare i post secondo liberi criteri, non solo per la home-page
    @Operation(summary = "Sorted by latest published post", description = "All posts are sorted by the latest post published", tags = {"Post"})
    public ResponseEntity<List<PostBoxResponse>> getLatestPostHomePage(
            @RequestParam(defaultValue = "0") int pageNumber, // numero di pagina da cui partire; 0 è la prima pagina
            @RequestParam(defaultValue = "3") int pageSize, // numero di elementi per pagina
            @RequestParam(defaultValue = "publishedAt") String sortBy, // la colonna presa in considerazione per l'ordinamento
            @RequestParam(defaultValue = "DESC") String direction // ASC o DESC, ordinamento ascendente o discendente
    ){
        return ResponseEntity.ok(postService.getLatestPostHomePage(pageNumber, pageSize, sortBy, direction, imagePath));
    }

    @PostMapping("/v1/posts/preferred/{postId}")
    //@PreAuthorize("hasAuthority('MEMBER')")
    @Operation(summary = "Add or remove posts favorites", description = "Ability to add or remove posts from favorites", tags = {"Post"})
    public ResponseEntity<String> addOrRemovePreferredPost(@AuthenticationPrincipal UserDetails userDetails,
                                                   @PathVariable int postId){
        return ResponseEntity.ok(postService.addPreferredPost(userDetails, postId));
    }

}
