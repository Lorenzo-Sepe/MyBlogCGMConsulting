package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import it.cgmconsulting.myblog.entity.enumeration.AuthorityName;
import it.cgmconsulting.myblog.entity.enumeration.ReactionName;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper = false)
public class Reaction extends CreationUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private byte id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, length = 8, nullable = false)
    private ReactionName reactionName;

    private boolean visible; // di default a true

}
