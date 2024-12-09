package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.MadeByYou;
import it.cgmconsulting.myblog.entity.Report;
import it.cgmconsulting.myblog.entity.enumeration.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    boolean existsByCommentAndStatusIsNot(Comment comment, ReportStatus status);
    Optional<Report> findByMadeByYou(MadeByYou madeByYou);
}
