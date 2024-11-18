package it.cgmconsulting.myblog.entity.enumeration;

import lombok.ToString;

@ToString
public enum AuthorityName {

    ADMIN,
    MEMBER, // scrive i commenti, vota i post, segnala i commenti altrui
    AUTHOR, // scrive i post
    MODERATOR, // si occupa delle segnalazioni
    GUEST; // di default all'iscrizione finché non conferma email

}
