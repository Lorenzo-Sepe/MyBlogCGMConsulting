package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.payload.response.TagResponse;
import it.cgmconsulting.myblog.service.TagService;
import it.cgmconsulting.myblog.utils.Msg;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/v1/tags")
@RequiredArgsConstructor
@Validated
public class TagControllerV1 {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<Tag> create(@RequestParam
            @NotBlank(message = "Tag cannot be blank or null")
            @Size(max = 30, min = 3, message="The value must be between 3 and 30 characters") String id){
        return new ResponseEntity<>(tagService.create(id.toUpperCase()), HttpStatus.CREATED);
    }


    @PatchMapping
    public ResponseEntity<Tag> switchVisibility(@RequestParam
                @NotBlank(message = "Tag cannot be blank or null")
                @Size(max = 30, min = 3, message="The value must be between 3 and 30 characters") String id){
        return new ResponseEntity<>(tagService.switchVisibility(id.toUpperCase()), HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<TagResponse>> getTags(){
        return new ResponseEntity<>(tagService.getTags(), HttpStatus.OK);
    }



}
