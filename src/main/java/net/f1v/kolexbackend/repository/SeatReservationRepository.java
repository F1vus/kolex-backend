package net.f1v.kolexbackend.repository;

import net.f1v.kolexbackend.entity.SeatReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatReservationRepository extends JpaRepository<SeatReservation, Long> {

    @Query("""
        SELECT sr
        FROM SeatReservation sr
        WHERE sr.seat.travel.id = :travelId
        AND (
            sr.status = net.f1v.kolexbackend.entity.ReservationStatus.PURCHASED
            OR (
                sr.status = net.f1v.kolexbackend.entity.ReservationStatus.HELD
                AND sr.expiresAt > CURRENT_TIMESTAMP
            )
        )
    """)
    List<SeatReservation> findActiveReservations(Long travelId);
}
