package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Tag extends CreationUpdate {

    @Id
    @EqualsAndHashCode.Include
    @Column(length = 30)
    private String id;

    private boolean visible; // di default true

    public Tag(String id) {
        this.id = id;
        this.visible = true;
    }
}
