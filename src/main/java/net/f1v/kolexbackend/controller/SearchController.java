package net.f1v.kolexbackend.controller;

import lombok.RequiredArgsConstructor;
import net.f1v.kolexbackend.dto.SeatStatusDTO;
import net.f1v.kolexbackend.dto.TravelOptionDTO;
import net.f1v.kolexbackend.repository.TravelRepository;
import net.f1v.kolexbackend.service.SeatService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final TravelRepository travelRepository;
    private final SeatService seatService;

    @GetMapping("/trains")
    public List<TravelOptionDTO> searchTrains(
            @RequestParam Long fromId,
            @RequestParam Long toId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTime) {

        LocalDateTime endTime = departureTime.toLocalDate().atTime(23, 59, 59);

        return travelRepository.findAvailableTravels(fromId, toId, departureTime, endTime);
    }

    @GetMapping("/seats")
    public List<SeatStatusDTO> getSeats(
            @RequestParam Long travelId,
            @RequestParam int startStop,
            @RequestParam int endStop) {
        return seatService.getSeatMap(travelId, startStop, endStop);
    }
}
