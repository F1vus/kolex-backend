package net.f1v.kolexbackend.repository;

import net.f1v.kolexbackend.entity.SeatReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatReservationRepository extends JpaRepository<SeatReservation, Long> {
    @Query("""
        SELECT COUNT(sr) FROM SeatReservation sr
        WHERE sr.seat.travel.id = :travelId
          AND sr.status IN ('HELD', 'PURCHASED')
          AND sr.startStopNumber < :endStop
          AND sr.endStopNumber > :startStop
    """)
    long countOverlapping(
            @Param("travelId") Long travelId,
            @Param("startStop") int startStop,
            @Param("endStop") int endStop
    );

    // Do zwrotu biletu szukamy rezerwacji powiązanej z biletem
    Optional<SeatReservation> findByTicketId(Long ticketId);
}