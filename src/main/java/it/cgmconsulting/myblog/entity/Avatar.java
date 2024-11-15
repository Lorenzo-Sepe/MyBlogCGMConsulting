package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Avatar extends CreationUpdate {

    @Id
    @EqualsAndHashCode.Include
    private int id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String filetype; // mime type

    @Lob
    @Column(nullable = false, columnDefinition = "BLOB")
    private byte[] data;

}
