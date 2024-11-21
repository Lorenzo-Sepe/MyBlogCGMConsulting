package it.cgmconsulting.myblog.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostRequest {

    @NotBlank @Size(max = 255, min = 1)
    private String title;
    @NotBlank @Size(max = 255, min = 50)
    private String overview;
    @NotBlank @Size(max = 10000, min = 200)
    private String content;
    @Size(max = 255)
    private String image;
}
