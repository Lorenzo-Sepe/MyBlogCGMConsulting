package it.cgmconsulting.myblog.payload.request;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReportReasonRequest {

    @NotBlank @Size(max = 30)
    private String reason;
    @FutureOrPresent
    private LocalDate endDate;
    @Min(1) @Max(36500) // 100 anni -> ban permanente
    private int severity;
}
