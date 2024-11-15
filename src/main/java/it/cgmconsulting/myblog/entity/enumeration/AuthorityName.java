package it.cgmconsulting.myblog.entity.enumeration;

public enum AuthorityName {

    ADMIN,
    ROLE_MEMBER, // scrive i commenti, vota i post
    ROLE_AUTHOR, // scrive i post
    ROLE_MODERATOR, // si occupa delle segnalazioni
    ROLE_GUEST // di default all'iscrizione finché non conferma email
}
