package net.f1v.kolexbackend.repository;

import net.f1v.kolexbackend.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByUserId(Long userId);
    Optional<Profile> findByIdAndUserId(Long profileId, Long userId);
}