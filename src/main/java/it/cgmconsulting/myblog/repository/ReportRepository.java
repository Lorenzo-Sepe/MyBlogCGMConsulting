package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.MadeByYou;
import it.cgmconsulting.myblog.entity.Report;
import it.cgmconsulting.myblog.entity.enumeration.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    boolean existsByCommentAndStatusIsNot(Comment comment, ReportStatus status);
    Optional<Report> findByMadeByYou(MadeByYou madeByYou);

    @Query(value="SELECT r FROM Report r " +
            "WHERE r.comment.id = :commentId OR r.madeByYou.id = :madeByYouId")
    Optional<Report> getReportByCommentOrMadeByYouId(int commentId, int madeByYouId);

}
