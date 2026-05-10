package net.f1v.kolexbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.f1v.kolexbackend.entity.states.ReservationStatus;
import net.f1v.kolexbackend.entity.Seat;
import net.f1v.kolexbackend.entity.SeatReservation;
import net.f1v.kolexbackend.repository.ProfileRepository;
import net.f1v.kolexbackend.repository.SeatRepository;
import net.f1v.kolexbackend.repository.SeatReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final SeatReservationRepository reservationRepository;
    private final SeatRepository seatRepository;
    private final ProfileRepository profileRepository;


    @Transactional
    public SeatReservation holdSeat(Long seatId, Long profileId, int start, int end) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        SeatReservation reservation = new SeatReservation();
        reservation.setSeat(seat);
        reservation.setProfile(profileRepository.getReferenceById(profileId));
        reservation.setStartStopNumber(start);
        reservation.setEndStopNumber(end);
        reservation.setStatus(ReservationStatus.HELD);
        reservation.setCreatedAt(LocalDateTime.now());

        reservation.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        try {
            return reservationRepository.save(reservation);
        } catch (Exception e) {

            throw new RuntimeException("This spot on the selected route has already been reserved");
        }
    }

    @Transactional
    public void cancelReservation(Long reservationId, Long profileId) {
        SeatReservation res = reservationRepository.findByIdAndProfileId(reservationId, profileId)
                .orElseThrow(() -> new RuntimeException("Reservation not found or access denied"));

        if (res.getStatus() == ReservationStatus.HELD) {
            reservationRepository.delete(res);
        } else {
            throw new RuntimeException("Cannot cancel already purchased ticket here");
        }
    }
}
