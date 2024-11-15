package it.cgmconsulting.myblog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode @Builder
public class ReportReasonId implements Serializable {

    @Column(nullable = false, length = 30)
    private String reason;

    @Column(nullable = false)
    private LocalDate startDate;
}
