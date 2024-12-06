package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper = false)
public class CommentReaction extends CreationUpdate {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private CommentReactionId commentReactionId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Reaction reaction;
}
