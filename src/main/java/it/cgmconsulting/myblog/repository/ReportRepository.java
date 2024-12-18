package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.MadeByYou;
import it.cgmconsulting.myblog.entity.Report;
import it.cgmconsulting.myblog.entity.enumeration.ReportStatus;
import it.cgmconsulting.myblog.payload.response.ReportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    boolean existsByCommentAndStatusIsNot(Comment comment, ReportStatus status);
    Optional<Report> findByMadeByYou(MadeByYou madeByYou);

    @Query(value="SELECT r FROM Report r " +
            "WHERE r.comment.id = :commentId OR r.madeByYou.id = :madeByYouId")
    Optional<Report> getReportByCommentOrMadeByYouId(int commentId, int madeByYouId);

    @Query(value="SELECT " +
            "r.id," +
            "r.comment_id AS commentId, " +
            "r.made_by_you_id AS madeByYouId, " +
            "CASE WHEN r.comment_id IS NOT NULL THEN u1.username ELSE u2.username END AS username, " +
            "(SELECT COUNT(ru.user_id) FROM reporter_users ru WHERE ru.report_id=r.id) AS counter, " +
            "r.status AS status, " +
            "r.reason, " +
            "r.created_at AS createdAt, " +
            "r.updated_at AS updatedAt " +
            "FROM report r " +
            "LEFT JOIN comment c ON c.id=r.comment_id " +
            "LEFT JOIN made_by_you m ON m.id = r.made_by_you_id " +
            "LEFT JOIN _user u1 ON u1.id=c.user_id " +
            "LEFT JOIN _user u2 ON u2.id=m.user_id " +
            "WHERE r.status = :status", nativeQuery = true)
    Page<ReportResponse> getReports(Pageable pageable, String status);

}
