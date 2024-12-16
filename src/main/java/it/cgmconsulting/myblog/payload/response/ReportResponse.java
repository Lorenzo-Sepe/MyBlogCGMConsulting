package it.cgmconsulting.myblog.payload.response;

import it.cgmconsulting.myblog.entity.enumeration.ReportStatus;

import java.time.LocalDateTime;


public interface ReportResponse {

    int getId();
    Integer getCommentId();
    Integer getMadeByYouId();
    String getUsername(); // l'autore del commento o del "Made by You" in esame
    int getCounter();
    ReportStatus getStatus();

    String getReason();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();

}
