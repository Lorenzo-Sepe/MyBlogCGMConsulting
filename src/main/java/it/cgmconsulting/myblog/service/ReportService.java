package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.*;
import it.cgmconsulting.myblog.entity.enumeration.ReportStatus;
import it.cgmconsulting.myblog.exception.BadRequestException;
import it.cgmconsulting.myblog.exception.ConflictException;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.request.ReportRequest;
import it.cgmconsulting.myblog.payload.response.ReportResponse;
import it.cgmconsulting.myblog.repository.CommentRepository;
import it.cgmconsulting.myblog.repository.MadeByYouRepository;
import it.cgmconsulting.myblog.repository.ReportReasonRepository;
import it.cgmconsulting.myblog.repository.ReportRepository;
import it.cgmconsulting.myblog.utils.Msg;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final CommentRepository commentRepository;
    private final MadeByYouRepository madeByYouRepository;
    private final ReportReasonRepository reportReasonRepository;

    @Transactional
    public boolean createReport(ReportRequest request, UserDetails userDetails) {
        int c = request.getCommentId();
        int m = request.getMadeByYouId();

        if(c == 0 && m == 0)
            throw new BadRequestException(Msg.REPORT_NO_PARAMETERS);
        if(c > 0 && m > 0)
            throw new BadRequestException(Msg.REPORT_DOUBLE_PARAMETERS);

        Comment comment = commentRepository.getValidComment(c);
        if(comment != null && comment.getUser().equals(userDetails))
            throw new ConflictException(Msg.REPORT_INVALID_REPORTER);
        MadeByYou madeByYou = madeByYouRepository.getValidMadeByYou(m);
        if(madeByYou != null && madeByYou.getUser().equals(userDetails))
            throw new ConflictException(Msg.REPORT_INVALID_REPORTER);

        Optional<Report> report = reportRepository.getReportByCommentOrMadeByYouId(c, m);
        if(report.isEmpty()) {
            reportRepository.save(Report.builder()
                    .status(ReportStatus.OPEN)
                    .comment(c > 0 ? comment : null)
                    .madeByYou(m > 0 ? madeByYou : null)
                    .reporters(Collections.singleton((User)userDetails))
                    .build());
            return true;
        } else {
            if(!report.get().getReporters().add((User)userDetails))
                throw new ConflictException(Msg.REPORT_ALREDY_REPORTED);
            return false;
        }
    }

    public List<ReportResponse> getReports(int pageNumber, int pageSize, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.valueOf(direction.toUpperCase()), sortBy);
        Page<ReportResponse> list = reportRepository.getReports(pageable);
        return list.getContent();
    }

    @Transactional
    public ReportResponse updateRecord(int reportId, String status, String reason) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(()-> new ResourceNotFoundException("Report", "id", reportId));

        ReportStatus reportStatus = report.getStatus();

        // il report è già stato chiuso quindi non posso più cambiargli stato
        if(reportStatus.name().startsWith("CLOSED_"))
            throw new BadRequestException(Msg.REPORT_UNMODIFIABLE);

        // Stesso status tra db e request
        if(reportStatus.equals(ReportStatus.valueOf(status)))
            throw new BadRequestException(Msg.REPORT_SAME_STATUS);

        // Sul db è il stato OPEN quindi può diventare solo IN PROGRESS
        if(reportStatus.equals(ReportStatus.OPEN))
            if(ReportStatus.valueOf(status).name().startsWith("CLOSED_"))
                throw new BadRequestException(Msg.REPORT_NOT_CLOSEABLE);
            else {
                report.setStatus(ReportStatus.valueOf(status));
            }

        // Sul db è il stato IN PROGRESS quindi posso solo chiuderlo
        if(reportStatus.equals(ReportStatus.IN_PROGRESS))
            if(ReportStatus.valueOf(status).equals(ReportStatus.OPEN))
                throw new BadRequestException(Msg.REPORT_COME_BACK_NOT_ALLOWED);
            else {
                // chiudo report senza ban
                if(ReportStatus.valueOf(status).equals(ReportStatus.CLOSED_WITHOUT_BAN))
                    report.setStatus(ReportStatus.valueOf(status));
                // oppure con ban
                else {
                    // trovare la reson valida da settare sul report
                    ReportReason reportReason = reportReasonRepository.getValidReason(reason)
                            .orElseThrow(()-> new ResourceNotFoundException("Reason", "name", reason));
                    report.setReportReason(reportReason);
                    // disabilitare l'utente e censurare il commento o il made by you in oggetto
                    if(report.getComment() != null) {
                        report.getComment().getUser().setEnabled(false);
                        report.getComment().setCensored(true);
                    }
                    else {
                        report.getMadeByYou().getUser().setEnabled(false);
                        report.getMadeByYou().setCensored(true);
                    }

                }
            }
/* todo */
        return null;
    }
}
