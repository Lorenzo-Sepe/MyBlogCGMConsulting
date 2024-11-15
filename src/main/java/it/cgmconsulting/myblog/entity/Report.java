package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import it.cgmconsulting.myblog.entity.enumeration.ReportStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Report extends CreationUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @ManyToOne
    private Comment comment;

    @ManyToOne
    private MadeByYou madeByYou;

    private int counter; // contatore di segnalazioni

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 18)
    private ReportStatus status;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="reason"),
            @JoinColumn(name="start_date")
    })
    private ReportReason reportReason;


}
