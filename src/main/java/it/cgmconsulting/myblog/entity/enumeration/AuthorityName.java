package it.cgmconsulting.myblog.entity.enumeration;

public enum AuthorityName {

    ADMIN,
    MEMBER, // scrive i commenti, vota i post
    AUTHOR, // scrive i post
    MODERATOR, // si occupa delle segnalazioni
    GUEST // di default all'iscrizione finché non conferma email
}
