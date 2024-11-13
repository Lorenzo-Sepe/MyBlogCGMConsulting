package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.payload.response.TagResponse;
import it.cgmconsulting.myblog.utils.Msg;
import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.service.TagService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/v1/tags")
@RequiredArgsConstructor
public class TagControllerV1 {

    private final TagService tagService;

    /**
     * Controller per Creazione di Tag di lunghezza massima fissa
     * @param id nome del tag da creare
     * @return response http del server
     */
    @PostMapping
    public ResponseEntity<Tag> create(@RequestParam
                                          @NotBlank(message = "The value must be between 3 and 30 characters")
                                          @Size(max=30,min=3,message = "The value must be between 3 and 30 characters")
                                          String id)
    {
        return new ResponseEntity<>(tagService.create(id.toUpperCase()), HttpStatus.CREATED);
    }


    //modifica visibilità
    @PatchMapping
    public ResponseEntity<Tag> switchVisibility(@RequestParam
                                                    @NotBlank(message = "The value must be between 3 and 30 characters")
                                                    @Size(max=30,min=3,message = "The value must be between 3 and 30 characters")
                                                    String id){
        return new ResponseEntity<>(tagService.switchVisibility(id.toUpperCase()), HttpStatus.OK);
    }

    //get all tag distinguendo tra visibile e non visibile
    @GetMapping
    public ResponseEntity<List<TagResponse>> getAll(){
        return new ResponseEntity<>(tagService.getTags(), HttpStatus.OK);
    }

}
