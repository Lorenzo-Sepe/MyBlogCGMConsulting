package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Byte> {

    // SElect all from authority where default_Authority = true and visible = true
    Optional<Authority> findByDefaultAuthorityTrueAndVisibleTrue();
}
