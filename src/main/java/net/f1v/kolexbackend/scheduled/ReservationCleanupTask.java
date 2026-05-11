package net.f1v.kolexbackend.scheduled;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.f1v.kolexbackend.entity.states.ReservationStatus;
import net.f1v.kolexbackend.entity.SeatReservation;
import net.f1v.kolexbackend.repository.SeatReservationRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class ReservationCleanupTask {

    private final SeatReservationRepository reservationRepository;

    // runs every minute
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();
        List<SeatReservation> expired = reservationRepository
                .findAllByStatusAndExpiresAtBefore(ReservationStatus.HELD, now);

        if (!expired.isEmpty()) {
            reservationRepository.deleteAll(expired);
            System.out.println("Expired reservations have been cleared: " + expired.size());
        }
    }
}