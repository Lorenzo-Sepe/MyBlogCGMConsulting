package it.cgmconsulting.myblog.payload.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PostBoxResponse {

    private int id;
    private String title;
    private String overview;
    private String author; // username dello user autore del post
    private String image;  // imagePath + nomefile.ext
    private LocalDate publishedAt;

    private long comments; // numero di commenti di un post
    private double rating; // voto medio di un post

    public PostBoxResponse(int id, String title, String overview, String author, String image, LocalDate publishedAt, long comments, double rating) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.author = author;
        this.image = image;
        this.publishedAt = publishedAt;
        this.comments = comments;
        this.rating = Math.round(rating * 10.0) / 10.0; // arrotondamento alla prima cifra decimale
    }
}
