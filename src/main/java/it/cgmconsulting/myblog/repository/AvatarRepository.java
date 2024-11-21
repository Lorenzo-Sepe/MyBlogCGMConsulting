package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Avatar;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AvatarRepository extends JpaRepository<Avatar, Integer> {

    @Modifying
    @Transactional
    @Query(value="DELETE FROM avatar WHERE id = :id", nativeQuery = true)
    void deleteAvatar(int id);
}
