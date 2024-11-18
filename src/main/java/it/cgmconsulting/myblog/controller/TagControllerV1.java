package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.entity.enumeration.AuthorityName;
import it.cgmconsulting.myblog.payload.response.TagResponse;
import it.cgmconsulting.myblog.service.TagService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class TagControllerV1 {

    private final TagService tagService;

    @PostMapping("/v1/tags")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Tag> create(@RequestParam
            @NotBlank(message = "Tag cannot be blank or null")
            @Size(max = 30, min = 3, message="The value must be between 3 and 30 characters") String id){
        return new ResponseEntity<>(tagService.create(id.toUpperCase()), HttpStatus.CREATED);
    }


    @PatchMapping("/v1/tags")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Tag> switchVisibility(@RequestParam
                @NotBlank(message = "Tag cannot be blank or null")
                @Size(max = 30, min = 3, message="The value must be between 3 and 30 characters") String id){
        return new ResponseEntity<>(tagService.switchVisibility(id.toUpperCase()), HttpStatus.OK);
    }

    @GetMapping("/v1/tags")
    @PreAuthorize("hasAuthority('ADMIN')") // localhost:8081/api/v0/tags?visible=
    public ResponseEntity<List<TagResponse>> getTags(@RequestParam Optional<Boolean> visible){
        return new ResponseEntity<>(tagService.getTags(visible), HttpStatus.OK);
    }

    @GetMapping("/v0/tags") // localhost:8081/api/v0/tags?visible=true
    public ResponseEntity<List<TagResponse>> getVisibleTags(@RequestParam Optional<Boolean> visible){
        return new ResponseEntity<>(tagService.getTags(visible), HttpStatus.OK);
    }

}
