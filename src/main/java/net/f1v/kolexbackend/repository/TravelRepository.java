package net.f1v.kolexbackend.repository;

import net.f1v.kolexbackend.entity.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {
    List<Travel> findByDepartureBetween(LocalDateTime from, LocalDateTime to);
}