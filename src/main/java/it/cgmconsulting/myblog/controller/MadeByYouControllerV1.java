package it.cgmconsulting.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.cgmconsulting.myblog.payload.response.MadeByYouResponse;
import it.cgmconsulting.myblog.service.MadeByYouService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MadeByYouControllerV1 {

    private final MadeByYouService madeByYouService;

    @Value("${application.image.madeByYou.size}")
    private long size;
    @Value("${application.image.madeByYou.width}")
    private int width;
    @Value("${application.image.madeByYou.height}")
    private int height;
    @Value("${application.image.madeByYou.extensions}")
    private String[]  extensions;
    @Value("${application.image.madeByYou.imagePath}")
    private String  imagePath;

    @Operation(
            summary = "ADD YOUR ARTIFACT",
            description = "Method that allow to upload an image of your artifact. One image per user per post.",
            tags = {"MadeByYou"})
    @PostMapping(value="/v1/madeByYou/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<MadeByYouResponse> addArtifact(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int postId,
            @RequestParam MultipartFile file,
            @RequestParam(required = false) String description
            ) throws IOException {
        return new ResponseEntity<MadeByYouResponse>(madeByYouService.addArtifact(userDetails, postId, file, description, size, width, height, extensions, imagePath), HttpStatus.CREATED);
    }

    /*
    Conflitto con Multipart: Stai usando un parametro MultipartFile, che implica una richiesta multipart/form-data.
    Con questo tipo di richiesta, il corpo della richiesta non viene gestito come JSON ma come parti separate (file e campi).
    Spring non può automaticamente leggere una String da @RequestBody in un contesto multipart.
     */

    @Operation(
            summary = "GET ARTIFACTS BY POST",
            description = "Method that returns paginated 'made by you' (4 elements by default)",
            tags = {"MadeByYou"})
    @GetMapping("/v0/madeByYou/{postId}")
    public ResponseEntity<List<MadeByYouResponse>> getArtifacts(
            @PathVariable int postId,
            @RequestParam(defaultValue = "0") int pageNumber, // numero di pagina da cui partire; 0 è la prima pagina
            @RequestParam(defaultValue = "4") int pageSize, // numero di elementi per pagina
            @RequestParam(defaultValue = "createdAt") String sortBy, // la colonna presa in considerazione per l'ordinamento
            @RequestParam(defaultValue = "DESC") String direction // ASC o DESC, ordinamento ascendente o discendente
    ){
        return ResponseEntity.ok(madeByYouService.getArtifacts(postId, pageNumber, pageSize, sortBy, direction, imagePath));
    }


    @Operation(
            summary = "DELETE ARTIFACT",
            description = "Method that remove user artifact. Only the artifact author can delete it." ,
            tags = {"MadeByYou"})
    @DeleteMapping("/v1/madeByYou/{id}")
    public ResponseEntity<String> deleteArtifact(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable int id){
        return ResponseEntity.ok(madeByYouService.deleteArtifact(userDetails, id, imagePath));
    }
}
