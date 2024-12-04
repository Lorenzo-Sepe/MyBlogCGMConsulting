package it.cgmconsulting.myblog.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentUpdateRequest {

    @NotBlank
    @Size(max = 255, min = 3)
    private String comment;
}
