package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.User;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsernameOrEmail(String username, String email);

    Optional<User> findByConfirmCode(String confirmCode);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    @Modifying
    @Query(value="UPDATE _user SET password = :password WHERE id = :id", nativeQuery = true)
    void updatePassword(String password, int id);

    boolean existsByUsernameAndIdNot(String username, int id);
    boolean existsByEmailAndIdNot(String email, int id);


}
