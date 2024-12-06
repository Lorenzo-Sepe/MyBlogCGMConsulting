package it.cgmconsulting.myblog.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class VwCountReactionByComment {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private VwCountReactionByCommentId vwCountReactionByCommentId;
    private long tot;
}
