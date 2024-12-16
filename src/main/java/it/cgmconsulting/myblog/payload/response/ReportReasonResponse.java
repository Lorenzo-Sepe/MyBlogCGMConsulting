package it.cgmconsulting.myblog.payload.response;

import it.cgmconsulting.myblog.entity.ReportReason;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class ReportReasonResponse {

    private String reason;
    private LocalDate startDate;
    private LocalDate endDate;
    private int severity;

    public static ReportReasonResponse fromEntityToDto(ReportReason rr){
        return new ReportReasonResponse(
                rr.getReportReasonId().getReason(),
                rr.getReportReasonId().getStartDate(),
                rr.getEndDate(),
                rr.getSeverity()
        );
    }
}
