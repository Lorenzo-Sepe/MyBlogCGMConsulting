package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.entity.Avatar;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import  org.springframework.security.core.userdetails.UserDetails;
import it.cgmconsulting.myblog.service.ImageService;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@Validated
@Slf4j


public class AvatarControllerV1 {

    private final ImageService imageService;

    @Value("${application.image.avatar.size}")
    private long size;
    @Value("${application.image.avatar.width}")
    private int width;
    @Value("${application.image.avatar.height}")
    private int height;
    @Value("${application.image.avatar.extensions}")
    private String[] extensions;

    @PostMapping(value = "/v1/avatars",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Avatar> addAvatar(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam MultipartFile file
    ) throws IOException {return ResponseEntity.ok(imageService.addAvatar(userDetails, file, size, width, height , extensions));}


}
