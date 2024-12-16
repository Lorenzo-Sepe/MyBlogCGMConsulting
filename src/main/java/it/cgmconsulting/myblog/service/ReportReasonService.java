package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.ReportReason;
import it.cgmconsulting.myblog.entity.ReportReasonId;
import it.cgmconsulting.myblog.exception.ConflictException;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.request.ReportReasonRequest;
import it.cgmconsulting.myblog.payload.response.ReportReasonResponse;
import it.cgmconsulting.myblog.repository.ReportReasonRepository;
import it.cgmconsulting.myblog.utils.Msg;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportReasonService {

    private final ReportReasonRepository reportReasonRepository;

    public ReportReasonResponse addReportReason(ReportReasonRequest request) {
        reportReasonRepository.getValidReason(request.getReason())
                .ifPresent(e -> {throw new ConflictException(Msg.REPORT_REASON_VALID_ALREADY_PRESENT);});
        ReportReason reportReason = new ReportReason(
                new ReportReasonId(request.getReason(), LocalDate.now()),
                null,
                request.getSeverity()
        );
        reportReasonRepository.save(reportReason);
        return ReportReasonResponse.fromEntityToDto(reportReason);
    }

    @Transactional
    public ReportReasonResponse updateReportReason(ReportReasonRequest request) {
        ReportReason reportReason = reportReasonRepository.getValidReason(request.getReason())
                .orElseThrow(()-> new ResourceNotFoundException("Reaport reason", "name", request.getReason()));

        // Update only severity creating another record and updating the end date of previous record
        ReportReason newReportReason = new ReportReason();
        if(request.getSeverity() != reportReason.getSeverity() && request.getEndDate() == null){
            reportReason.setEndDate(LocalDate.now().minusDays(1L));
            newReportReason.setEndDate(null);
            newReportReason.setSeverity(request.getSeverity());
            newReportReason.setReportReasonId(new ReportReasonId(request.getReason(), LocalDate.now()));
            reportReasonRepository.save(newReportReason);
            return ReportReasonResponse.fromEntityToDto(newReportReason);
        }

        // Update only the end date in order to set an expiration date
        if(request.getEndDate() != null){
            reportReason.setEndDate(request.getEndDate());
        }
        return ReportReasonResponse.fromEntityToDto(reportReason);
    }

    public List<ReportReasonResponse> getValidReasons(){
        return reportReasonRepository.getValidReasons();
    }
}
