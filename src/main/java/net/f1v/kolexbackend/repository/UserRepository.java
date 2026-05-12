package net.f1v.kolexbackend.repository;

import jakarta.transaction.Transactional;
import net.f1v.kolexbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User u " +
            "SET u.email = 'deleted_' || u.id || '_' || u.email, u.password = 'DELETED'," +
            "    u.enabled = false \n" +
            "WHERE u.id = :userId")
    void deactivateUserById(@Param("userId") Long userId);
}