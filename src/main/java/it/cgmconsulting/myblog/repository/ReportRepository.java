package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.Report;
import it.cgmconsulting.myblog.entity.enumeration.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    boolean existsByCommentAndStatusIsNot(Comment comment, ReportStatus status);
}
