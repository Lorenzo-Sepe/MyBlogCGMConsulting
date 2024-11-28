package it.cgmconsulting.myblog.payload.response;

import it.cgmconsulting.myblog.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor @AllArgsConstructor
public class PostResponse {

    private int id;
    private String title;
    private String overview;
    private String content;
    private String author; // username dello user autore del post
    private String image;  // imagePath + nomefile.ext
    private LocalDate publishedAt;
    private Set<String> tags = new HashSet<>();

    public PostResponse(int id, String title, String overview, String content, String author, String image, LocalDate publishedAt) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.content = content;
        this.author = author;
        this.image = image;
        this.publishedAt = publishedAt;
    }

    public static PostResponse fromEntityToDto(Post post, String imagePath){
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getOverview(),
                post.getContent(),
                post.getUser().getUsername(),
                post.getImage() != null ? imagePath+post.getImage() : null,
                post.getPublishedAt()
        );
    }
}
