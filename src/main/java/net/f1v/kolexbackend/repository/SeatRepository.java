package net.f1v.kolexbackend.repository;

import net.f1v.kolexbackend.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByTravel_Id(Long travelId);
}
