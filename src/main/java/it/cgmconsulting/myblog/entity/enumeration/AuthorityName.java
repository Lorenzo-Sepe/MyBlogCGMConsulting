package it.cgmconsulting.myblog.entity.enumeration;

public enum AuthorityName {
    ADMIN,
    MEMBER, // scrive i commenti, vota i post
    MODERATOR, // si occupa delle segnalazioni
    GUEST // di default all'iscrizione
}