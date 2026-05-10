package net.f1v.kolexbackend.repository;

import net.f1v.kolexbackend.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByTravel_Id(Long travelId);

    @Query(value = """
        SELECT s.* FROM backend.seat s
        WHERE s.travel_id = :travelId
        AND s.seat_id NOT IN (
            SELECT sr.seat_id FROM backend.seat_reservation sr
            WHERE sr.status IN ('HELD', 'PURCHASED')
            AND NOT (sr.end_stop_number <= :userStart OR sr.start_stop_number >= :userEnd)
        )
        LIMIT 1
        """, nativeQuery = true)
    Optional<Seat> findFirstAvailableSeat(Long travelId, int userStart, int userEnd);
}
