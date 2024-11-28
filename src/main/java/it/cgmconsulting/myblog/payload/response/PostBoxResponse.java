package it.cgmconsulting.myblog.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostBoxResponse {
    private int id;
    private String title;
    private String overview;
    private String author;
    private String image;

    private long comments; //numero commenti
    private double rating; //media voti
}
