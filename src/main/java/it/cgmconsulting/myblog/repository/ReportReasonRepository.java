package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.ReportReason;
import it.cgmconsulting.myblog.entity.ReportReasonId;
import it.cgmconsulting.myblog.payload.response.ReportReasonResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReportReasonRepository extends JpaRepository<ReportReason, ReportReasonId> {

        @Query(value="SELECT rr FROM ReportReason rr " +
                "WHERE rr.reportReasonId.reason = :reason " +
                "AND (" +
                        "rr.endDate IS NULL " +
                        "OR (CURRENT_DATE() BETWEEN rr.reportReasonId.startDate AND rr.endDate)" +
                    ")"
        )
        Optional<ReportReason> getValidReason(String reason);

        @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.ReportReasonResponse(" +
                "rr.reportReasonId.reason, " +
                "rr.reportReasonId.startDate, " +
                "rr.endDate, " +
                "rr.severity) " +
                "FROM ReportReason rr " +
                "WHERE rr.endDate IS NULL OR rr.endDate >= CURRENT_DATE()"
        )
        List<ReportReasonResponse> getValidReasons();
}
