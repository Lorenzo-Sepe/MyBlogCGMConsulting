package it.cgmconsulting.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.cgmconsulting.myblog.entity.Avatar;
import it.cgmconsulting.myblog.payload.response.AvatarResponse;
import it.cgmconsulting.myblog.payload.response.UserResponse;
import it.cgmconsulting.myblog.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AvatarControllerV1 {

    private final ImageService imageService;

    @Value("${application.image.avatar.size}")
    private long size;
    @Value("${application.image.avatar.width}")
    private int width;
    @Value("${application.image.avatar.height}")
    private int height;
    @Value("${application.image.avatar.extensions}")
    private String[]  extensions;



    @Operation(
            summary = "ADD AVATAR",
            description = "Method to add an avatar to the user in the database",
            tags = {"Avatar"})
    @PostMapping(value="/v1/avatars", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AvatarResponse> addAvatar(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam MultipartFile file
            ) throws IOException {
        return ResponseEntity.ok(imageService.addAvatar(userDetails, file, size, width, height, extensions));

    }

    @Operation(
            summary = "REMOVE AVATAR",
            description = "Method to remove the avatar of the user in the database",
            tags = {"Avatar"})
    @DeleteMapping("/v1/avatars")
    public ResponseEntity<UserResponse> removeAvatar(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(imageService.removeAvatar(userDetails));
    }

}
