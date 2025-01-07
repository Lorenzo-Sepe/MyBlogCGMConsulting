package it.cgmconsulting.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.cgmconsulting.myblog.exception.InternalServerErrorException;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.service.PdfService;
import it.cgmconsulting.myblog.utils.Msg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
@RequiredArgsConstructor
public class PdfControllerV1 {

    private final PdfService pdfService;
    @Value("${application.image.post.imagePath}")
    private String imagePath;

    @Operation(summary = "Generate a pdf from post",
            description = "Generate a pdf from post",
            tags = {"Pdf"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pdf successfully generated"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Error creating pdf")
    })
    @GetMapping("/v0/pdf/{postId}") // localhost:8081/api/v0/pdf/3
    public ResponseEntity<InputStreamResource> createPdf(@PathVariable int postId){

        try {
            InputStream pdfFile = pdfService.createPdf(postId, imagePath);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF); // Content-Type : application/pdf
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Method", "GET");
            headers.add("Access-Control-Allow-Headers", "Content-Type");
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-disposition", "inline; filename="+postId+".pdf");
            return new ResponseEntity<InputStreamResource>(new InputStreamResource(pdfFile), headers, HttpStatus.OK);
        } catch (ResourceNotFoundException ex){
            throw new ResourceNotFoundException("Post", "id", postId);
        } catch (Exception e){
            throw new InternalServerErrorException(Msg.PDF_FATAL_ERROR);
        }
    }

}
