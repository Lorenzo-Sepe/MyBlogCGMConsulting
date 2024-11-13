package it.cgmconsulting.myblog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ReportReasonId implements Serializable {

    @Column(nullable = false, length = 30)
    private String reason;

    @Column(nullable = false)
    private LocalDate startDate;
}
