package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class ReportReason extends CreationUpdate {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private ReportReasonId reportReasonId;

    private LocalDate endDate = LocalDate.MAX;

    private int severity;
}