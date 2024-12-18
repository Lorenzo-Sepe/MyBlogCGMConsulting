package it.cgmconsulting.myblog.payload.response;

import it.cgmconsulting.myblog.entity.Report;
import it.cgmconsulting.myblog.entity.enumeration.ReportStatus;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor @Data
public class ReportResponseImpl implements ReportResponse{

    private int id;
    private Integer commentId;
    private Integer madeByYouId;
    private String username;
    private int counter;
    private ReportStatus status;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

/*
    @Override
    public int getId() {
        return id;
    }

    @Override
    public Integer getCommentId() {
        return commentId;
    }

    @Override
    public Integer getMadeByYouId() {
        return madeByYouId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public int getCounter() {
        return 0;
    }

    @Override
    public ReportStatus getStatus() {
        return status;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
*/
    public static ReportResponseImpl fromEntityToDto(Report report){
        return new ReportResponseImpl(
                report.getId(),
                report.getComment() == null ? null : report.getComment().getId(),
                report.getMadeByYou() == null ? null : report.getMadeByYou().getId(),
                report.getComment() != null ? report.getComment().getUser().getUsername() : report.getMadeByYou().getUser().getUsername(),
                report.getReporters().size(),
                report.getStatus(),
                report.getReportReason() == null ? null : report.getReportReason().getReportReasonId().getReason(),
                report.getCreatedAt(),
                report.getUpdatedAt()
        );

    }
}
