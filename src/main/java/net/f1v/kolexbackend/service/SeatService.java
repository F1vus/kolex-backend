package net.f1v.kolexbackend.service;

import lombok.RequiredArgsConstructor;
import net.f1v.kolexbackend.dto.SeatStatusDTO;
import net.f1v.kolexbackend.entity.Seat;
import net.f1v.kolexbackend.entity.SeatReservation;
import net.f1v.kolexbackend.repository.SeatRepository;
import net.f1v.kolexbackend.repository.SeatReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final SeatReservationRepository reservationRepository;

    public List<SeatStatusDTO> getSeatMap(Long travelId, int userStart, int userEnd) {
        List<Seat> allSeats = seatRepository.findByTravel_Id(travelId);

        List<SeatReservation> activeReservations = reservationRepository.findActiveReservations(travelId);

        return allSeats.stream().map(seat -> {
            boolean isTaken = activeReservations.stream()
                    .filter(res -> res.getSeat().getId().equals(seat.getId()))
                    .anyMatch(res -> overlaps(res.getStartStopNumber(), res.getEndStopNumber(), userStart, userEnd));

            return new SeatStatusDTO(seat.getId(), seat.getSeatNumber(), !isTaken);
        }).collect(Collectors.toList());
    }

    private boolean overlaps(int start1, int end1, int start2, int end2) {
        return Math.max(start1, start2) < Math.min(end1, end2);
    }
}