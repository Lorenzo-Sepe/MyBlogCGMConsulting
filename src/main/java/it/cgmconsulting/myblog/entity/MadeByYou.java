package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "post_id" }) })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class MadeByYou extends CreationUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(nullable = false)
    private String image;

    @Column(length = 100)
    private String description;

    private boolean censored; // di default false

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Post post;

}
