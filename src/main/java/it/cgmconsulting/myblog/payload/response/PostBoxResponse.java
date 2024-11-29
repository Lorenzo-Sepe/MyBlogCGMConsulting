package it.cgmconsulting.myblog.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor @AllArgsConstructor
public class PostBoxResponse {

    private int id;
    private String title;
    private String overview;
    private String author; // username dello user autore del post
    private String image;  // imagePath + nomefile.ext
    private LocalDate publishedAt;

    private long comments; // numero di commenti di un post
    private double rating; // voto medio di un post

}
