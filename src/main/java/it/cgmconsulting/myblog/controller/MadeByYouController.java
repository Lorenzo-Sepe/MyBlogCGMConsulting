package it.cgmconsulting.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.cgmconsulting.myblog.entity.MadeByYou;
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

@RestController
@RequiredArgsConstructor
public class MadeByYouController {

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
            description = "Method that allow to upload an image of your artifact",
            tags = {"MadeByYou"})
    @PostMapping(value="/v1/madeByYou/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<MadeByYouResponse> addArtifact(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int postId,
            @RequestParam MultipartFile file,
            @RequestBody(required = false) String description
            ) throws IOException {
        return new ResponseEntity<MadeByYouResponse>(madeByYouService.addArtifact(userDetails, postId, file, description, size, width, height, extensions, imagePath), HttpStatus.CREATED);
    }
}
