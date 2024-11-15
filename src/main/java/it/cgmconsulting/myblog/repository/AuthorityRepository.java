package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Authority;
import it.cgmconsulting.myblog.entity.enumeration.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Byte> {

    Optional<Authority> findByDefaultAuthorityTrueAndVisibleTrue();
    // SELECT * FROM authority WHERE default_authority = true AND visible = true;

    Optional<Authority> findByAuthorityNameAndVisibleTrue(AuthorityName authorityName);
    // SELECT * FROM authority WHERE authority_name = 'MEMBER' AND visible = true;
}
