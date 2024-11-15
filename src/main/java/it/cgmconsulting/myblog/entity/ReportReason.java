package it.cgmconsulting.myblog.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReportReason {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private ReportReasonId reportReasonId;

    private LocalDate endDate;

    private int severity;

}
