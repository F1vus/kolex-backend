package net.f1v.kolexbackend.repository;

import net.f1v.kolexbackend.entity.states.ReservationStatus;
import net.f1v.kolexbackend.entity.SeatReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatReservationRepository extends JpaRepository<SeatReservation, Long> {

    @Query("""
        SELECT sr
        FROM SeatReservation sr
        WHERE sr.seat.travel.id = :travelId
        AND (
            sr.status = net.f1v.kolexbackend.entity.states.ReservationStatus.PURCHASED
            OR (
                sr.status = net.f1v.kolexbackend.entity.states.ReservationStatus.HELD
                AND sr.expiresAt > CURRENT_TIMESTAMP
            )
        )
    """)
    List<SeatReservation> findActiveReservations(Long travelId);

    List<SeatReservation> findAllByStatusAndExpiresAtBefore(ReservationStatus status, LocalDateTime now);

    Optional<SeatReservation> findByIdAndProfileId(Long reservationId, Long profileId);

    void deleteByTicketId(Long id);
}
