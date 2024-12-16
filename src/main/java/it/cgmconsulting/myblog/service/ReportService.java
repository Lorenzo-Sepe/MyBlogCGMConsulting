package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.MadeByYou;
import it.cgmconsulting.myblog.entity.Report;
import it.cgmconsulting.myblog.entity.enumeration.ReportStatus;
import it.cgmconsulting.myblog.exception.BadRequestException;
import it.cgmconsulting.myblog.payload.request.ReportRequest;
import it.cgmconsulting.myblog.repository.CommentRepository;
import it.cgmconsulting.myblog.repository.MadeByYouRepository;
import it.cgmconsulting.myblog.repository.ReportRepository;
import it.cgmconsulting.myblog.utils.Msg;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final CommentRepository commentRepository;
    private final MadeByYouRepository madeByYouRepository;

    @Transactional
    public boolean createReport(ReportRequest request) {
        int c = request.getCommentId();
        int m = request.getMadeByYouId();

        if(c == 0 && m == 0)
            throw new BadRequestException(Msg.REPORT_NO_PARAMETERS);
        if(c > 0 && m > 0)
            throw new BadRequestException(Msg.REPORT_DOUBLE_PARAMETERS);

        Comment comment = commentRepository.getValidComment(c);
        MadeByYou madeByYou = madeByYouRepository.getValidMadeByYou(m);

        Optional<Report> report = reportRepository.getReportByCommentOrMadeByYouId(c, m);
        if(report.isEmpty()) {
            reportRepository.save(Report.builder()
                    .counter(1)
                    .status(ReportStatus.OPEN)
                    .comment(c > 0 ? comment : null)
                    .madeByYou(m > 0 ? madeByYou : null)
                    .build());
            return true;
        } else {
            report.get().setCounter(report.get().getCounter() + 1);
            return false;
        }
    }
}
