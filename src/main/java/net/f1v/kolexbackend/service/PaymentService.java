package net.f1v.kolexbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.f1v.kolexbackend.dto.BuyTicketResponse;
import net.f1v.kolexbackend.entity.*;
import net.f1v.kolexbackend.error.exceptions.BusinessException;
import net.f1v.kolexbackend.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final SeatReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TravelRouteRepository routeRepository;
    private final SeatRepository seatRepository;
    private final ProfileRepository profileRepository;
    private final TravelRepository travelRepository;

    @Transactional
    public BuyTicketResponse purchaseRandomTicket(Long travelId, Long userId, Long profileId, int start, int end) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found", HttpStatus.NOT_FOUND));

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new BusinessException("Profile not found", HttpStatus.NOT_FOUND));

        if (!profile.getUser().getId().equals(userId)) {
            throw new BusinessException("This profile does not belong to the current user", HttpStatus.FORBIDDEN);
        }

        BigDecimal totalPrice = calculatePrice(travelId, start, end);

        if (user.getBalance().compareTo(totalPrice) < 0) {
            throw new BusinessException("Insufficient funds in the internal balance", HttpStatus.PAYMENT_REQUIRED);
        }

        Optional<Seat> availableSeat = seatRepository.findFirstAvailableSeat(travelId, start, end);

        user.setBalance(user.getBalance().subtract(totalPrice));
        userRepository.save(user);

        Travel travel = travelRepository.getReferenceById(travelId);
        TravelRoute travelRoute = routeRepository.findByTravelIdAndTravelStopNumber(travelId, start);
        LocalDateTime departure = travel.getDeparture().plus(travelRoute.getDepartureOffset());

        Ticket ticket = new Ticket();
        ticket.setTravel(travel);
        ticket.setProfile(profile);
        ticket.setPrice(totalPrice);
        ticket.setDepartureDate(departure);
        ticket.setStartStopNumber(start);
        ticket.setEndStopNumber(end);
        ticket.setCreatedAt(LocalDateTime.now());

        availableSeat.ifPresent(ticket::setSeat);
        ticket = ticketRepository.save(ticket);

        if (availableSeat.isPresent()) {
            SeatReservation res = new SeatReservation();
            res.setSeat(availableSeat.get());
            res.setProfile(profile);
            res.setTicket(ticket);
            res.setStartStopNumber(start);
            res.setEndStopNumber(end);
            res.setStatus(ReservationStatus.PURCHASED);
            res.setExpiresAt(LocalDateTime.now());
            reservationRepository.save(res);
        }

        String message = availableSeat.map(
                seat -> "Ticket purchased for " + profile.getFirstName() + " with seat #" + seat.getSeatNumber()
                ).orElseGet(() -> "Standing ticket purchased for " + profile.getFirstName());

        return new BuyTicketResponse(ticket.getId(), message);
    }

    @Transactional
    public BuyTicketResponse purchaseTicket(Long reservationId, Long userId) {
        // 1. Retrieve the reservation and check its validity
        SeatReservation res = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException("No reservation found", HttpStatus.NOT_FOUND));

        // Checking if the seat is already taken/paid
        if (res.getStatus() != ReservationStatus.HELD) {
            throw new BusinessException("This reservation has already been processed or is no longer valid", HttpStatus.CONFLICT);
        }

        // Checking the 10-minute timeout
        if (res.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("The payment period has expired", HttpStatus.GONE);
        }

        // 2. Calculate the cost
        BigDecimal totalPrice = calculatePrice(res);

        // 3. Check the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found", HttpStatus.NOT_FOUND));

        // 4. Check balance
        if (user.getBalance().compareTo(totalPrice) < 0) {
            throw new BusinessException("Insufficient funds in the internal balance", HttpStatus.PAYMENT_REQUIRED);
        }

        // 5. Withdrawal of funds
        user.setBalance(user.getBalance().subtract(totalPrice));
        userRepository.save(user);

        Travel travel = res.getSeat().getTravel();
        TravelRoute travelRoute = routeRepository.findByTravelIdAndTravelStopNumber(travel.getId(), res.getStartStopNumber());
        LocalDateTime departure = travel.getDeparture().plus(travelRoute.getDepartureOffset());


        // 6. Creating a ticket
        Ticket ticket = new Ticket();
        ticket.setTravel(travel);
        ticket.setProfile(res.getProfile());
        ticket.setPrice(totalPrice);
        ticket.setDepartureDate(departure);
        ticket.setStartStopNumber(res.getStartStopNumber());
        ticket.setEndStopNumber(res.getEndStopNumber());
        ticket.setCreatedAt(LocalDateTime.now());
        ticket = ticketRepository.save(ticket);

        // 7. Finalizing the reservation
        res.setStatus(ReservationStatus.PURCHASED);
        res.setTicket(ticket);
        reservationRepository.save(res);

        return new BuyTicketResponse(ticket.getId(), "Ticket purchased!");
    }

    private BigDecimal calculatePrice(SeatReservation res) {
        Long travelId = res.getSeat().getTravel().getId();
        // Retrieve prices from the travel_route table for the starting and ending stops
        BigDecimal startPrice = routeRepository.findByTravelIdAndTravelStopNumber(travelId, res.getStartStopNumber()).getPrice();
        BigDecimal endPrice = routeRepository.findByTravelIdAndTravelStopNumber(travelId, res.getEndStopNumber()).getPrice();
        return endPrice.subtract(startPrice);
    }

    private BigDecimal calculatePrice(Long travelId, int start, int end) {
        // Retrieve prices from the travel_route table for the starting and ending stops
        BigDecimal startPrice = routeRepository.findByTravelIdAndTravelStopNumber(travelId, start).getPrice();
        BigDecimal endPrice = routeRepository.findByTravelIdAndTravelStopNumber(travelId, end).getPrice();
        return endPrice.subtract(startPrice);
    }
}
