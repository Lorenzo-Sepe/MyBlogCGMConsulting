package it.cgmconsulting.myblog.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class VwCountReactionByCommentId implements Serializable {

    private int commentId;
    private String reactionName;
}
