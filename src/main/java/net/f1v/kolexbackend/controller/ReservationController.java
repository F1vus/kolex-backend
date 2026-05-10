package net.f1v.kolexbackend.controller;

import lombok.RequiredArgsConstructor;
import net.f1v.kolexbackend.dto.ReservationRequest;
import net.f1v.kolexbackend.dto.ReservationResponse;
import net.f1v.kolexbackend.entity.SeatReservation;
import net.f1v.kolexbackend.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/hold")
    public ReservationResponse hold(@RequestBody ReservationRequest req) {
        SeatReservation seatReservation = reservationService.holdSeat(req.getSeatId(), req.getProfileId(), req.getStartStopNumber(), req.getEndStopNumber());
        return new ReservationResponse(seatReservation.getId());
    }

    @DeleteMapping("/{reservationId}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long reservationId, @RequestParam Long profileId) {
        reservationService.cancelReservation(reservationId, profileId);
        return ResponseEntity.noContent().build();
    }
}
