package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.Check;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Rating extends CreationUpdate {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private RatingId ratingId;

    @Check(constraints = "rate > 0 AND rate < 6") // Check -> vincolo sul db per cui è impossibile inserire valori al di fuori del range
    private byte rate; // espresso in stelle da 1 a 5
}