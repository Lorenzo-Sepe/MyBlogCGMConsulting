package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Reaction;
import it.cgmconsulting.myblog.entity.enumeration.ReactionName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Byte> {

    Optional<Reaction> findByReactionName(ReactionName reactionName);
}
