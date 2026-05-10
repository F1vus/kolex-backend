package net.f1v.kolexbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.f1v.kolexbackend.config.jwtConfig.UserPrincipal;
import net.f1v.kolexbackend.dto.TicketResponseDto;
import net.f1v.kolexbackend.entity.Ticket;
import net.f1v.kolexbackend.entity.Travel;
import net.f1v.kolexbackend.service.TicketService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Slf4j
public class TicketController {
    private final TicketService ticketService;

    @GetMapping
    public List<TicketResponseDto> getAllTickets(@AuthenticationPrincipal UserPrincipal userPrincipal) {


        List<TicketResponseDto> ticketResponseDtos = ticketService.getAllTickets(userPrincipal.getId()).stream()
                .map(this::mapToDto)
                .toList();

        log.info("getAllTickets {}", ticketResponseDtos);

        return ticketResponseDtos;
    }

    private TicketResponseDto mapToDto(Ticket ticket) {
        Travel travel = ticket.getTravel();

        String fromStation = travel.getRoutes().stream()
                .filter(r -> r.getId().getTravelStopNumber() == ticket.getStartStopNumber())
                .map(r -> r.getStation().getName())
                .findFirst()
                .orElse("Unknown Station");

        String toStation = travel.getRoutes().stream()
                .filter(r -> r.getId().getTravelStopNumber() == ticket.getStartStopNumber())
                .map(r -> r.getStation().getName())
                .findFirst()
                .orElse("Unknown Station");

        return TicketResponseDto.builder()
                .id(ticket.getId())
                .trainName(travel.getTrain())
                .profileName(ticket.getProfile().getFirstName() + " " + ticket.getProfile().getLastName())
                .fromStationName(fromStation)
                .toStationName(toStation)
                .seatNumber(ticket.getSeat() != null ? ticket.getSeat().getSeatNumber() : null)
                .actualDeparture(travel.getDeparture().toString())
                .actualArrival(calculateArrivalTime(travel, ticket.getEndStopNumber()).toString())
                .price(ticket.getPrice())
                .travelStopNumberFrom(ticket.getStartStopNumber())
                .travelStopNumberTo(ticket.getEndStopNumber())
                .build();
    }

    private LocalDateTime calculateArrivalTime(Travel travel, Integer stopNumber) {
        return travel.getRoutes().stream()
                .filter(r -> r.getId().getTravelStopNumber().equals(stopNumber))
                .findFirst()
                .map(route -> travel.getDeparture().plus(route.getArrivalOffset()))
                .orElse(null);
    }
}