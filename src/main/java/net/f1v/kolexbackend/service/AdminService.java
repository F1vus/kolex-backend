package net.f1v.kolexbackend.service;

import lombok.RequiredArgsConstructor;
import net.f1v.kolexbackend.dto.admin.StopFormDto;
import net.f1v.kolexbackend.dto.admin.TravelFormDto;
import net.f1v.kolexbackend.entity.*;
import net.f1v.kolexbackend.entity.states.UserRole;
import net.f1v.kolexbackend.repository.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final TravelRepository travelRepository;
    private final StationRepository stationRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    @Transactional(readOnly = true)
    public List<Travel> getAllTravels() {
        return travelRepository.findAllWithRoutes().stream()
                .sorted(Comparator.comparing(Travel::getDeparture))
                .toList();
    }

    @Transactional
    public void saveTravel(TravelFormDto dto) {
        if (dto.getDeparture() == null) {
            throw new RuntimeException("Podaj datę i godzinę odjazdu z pierwszej stacji.");
        }

        List<StopFormDto> stops = sanitizeStops(dto.getStops());
        dto.setStops(stops);
        if (stops.isEmpty()) {
            throw new RuntimeException(
                    "Dodaj co najmniej jeden przystanek i wybierz stację. Puste wiersze są pomijane.");
        }
        if (dto.getSeatCount() == null || dto.getSeatCount() < 1) {
            throw new RuntimeException("Podaj liczbę miejsc (co najmniej 1)");
        }

        Travel travel;
        if (dto.getId() != null) {
            travel = travelRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Nie znaleziono podróży"));
            travel.getRoutes().clear();
        } else {
            travel = new Travel();
            travel.setRoutes(new ArrayList<>());
            travel.setSeats(new ArrayList<>());
        }

        travel.setTrain(dto.getTrain());
        travel.setDeparture(dto.getDeparture());

        LocalDateTime base = travel.getDeparture();
        Duration maxDuration = Duration.ZERO;

        List<StopFormDto> orderedStops = dto.getStops().stream()
                .sorted(Comparator.comparing(StopFormDto::getStopNumber, Comparator.nullsLast(Integer::compareTo)))
                .toList();

        int fallback = 1;
        for (int i = 0; i < orderedStops.size(); i++) {
            StopFormDto stopDto = orderedStops.get(i);
            if (stopDto.getArrivalTime() == null || stopDto.getDepartureTime() == null) {
                int label = stopDto.getStopNumber() != null ? stopDto.getStopNumber() : (i + 1);
                throw new RuntimeException(
                        "Uzupełnij datę i godzinę przyjazdu oraz odjazdu dla przystanku nr " + label + ".");
            }

            Station station = stationRepository.findById(stopDto.getStationId())
                    .orElseThrow(() -> new RuntimeException("Nie znaleziono stacji"));

            int stopNumber = stopDto.getStopNumber() != null ? stopDto.getStopNumber() : fallback++;

            Duration arr = Duration.between(base, stopDto.getArrivalTime());
            Duration dep = Duration.between(base, stopDto.getDepartureTime());

            if (arr.isNegative() || dep.isNegative()) {
                throw new RuntimeException(
                        "Przyjazd i odjazd na stacji muszą być w czasie nie wcześniejszym niż odjazd pociągu z pierwszej stacji");
            }
            if (dep.compareTo(arr) < 0) {
                throw new RuntimeException("Odjazd z stacji nie może być wcześniejszy niż przyjazd");
            }

            TravelRoute route = new TravelRoute();
            TravelRouteId routeId = new TravelRouteId();
            routeId.setTravelStopNumber(stopNumber);
            route.setId(routeId);
            route.setTravel(travel);
            route.setStation(station);
            route.setDistance(stopDto.getDistance());
            route.setPrice(stopDto.getPrice());

            route.setArrivalOffset(arr);
            route.setDepartureOffset(dep);

            if (arr.compareTo(maxDuration) > 0) {
                maxDuration = arr;
            }
            if (dep.compareTo(maxDuration) > 0) {
                maxDuration = dep;
            }

            travel.getRoutes().add(route);
        }

        travel.setTravelDuration(maxDuration);

        applySeats(travel, dto.getSeatCount());

        travelRepository.save(travel);
    }

    private void applySeats(Travel travel, int seatCount) {
        Long travelId = travel.getId();
        boolean hasTickets = travelId != null && ticketRepository.existsByTravel_Id(travelId);

        if (travel.getSeats() == null) {
            travel.setSeats(new ArrayList<>());
        }

        if (travelId == null) {
            for (int i = 1; i <= seatCount; i++) {
                Seat seat = new Seat();
                seat.setTravel(travel);
                seat.setSeatNumber(i);
                travel.getSeats().add(seat);
            }
            return;
        }

        if (!hasTickets) {
            travel.getSeats().clear();
            for (int i = 1; i <= seatCount; i++) {
                Seat seat = new Seat();
                seat.setTravel(travel);
                seat.setSeatNumber(i);
                travel.getSeats().add(seat);
            }
            return;
        }

        int current = travel.getSeats().size();
        if (seatCount < current) {
            throw new RuntimeException("Nie można zmniejszyć liczby miejsc — istnieją bilety na ten przejazd.");
        }
        for (int n = current + 1; n <= seatCount; n++) {
            Seat seat = new Seat();
            seat.setTravel(travel);
            seat.setSeatNumber(n);
            travel.getSeats().add(seat);
        }
    }

    @Transactional(readOnly = true)
    public TravelFormDto getTravelForEdit(Long id) {
        Travel t = travelRepository.findWithRoutesById(id).orElseThrow();
        TravelFormDto dto = new TravelFormDto();
        dto.setId(t.getId());
        dto.setTrain(t.getTrain());
        dto.setDeparture(t.getDeparture());
        dto.setSeatCount(t.getSeats() != null ? t.getSeats().size() : 0);

        List<TravelRoute> routes = new ArrayList<>(t.getRoutes());
        routes.sort(Comparator.comparing(r -> r.getId().getTravelStopNumber()));

        LocalDateTime base = t.getDeparture();
        for (TravelRoute r : routes) {
            StopFormDto s = new StopFormDto();
            s.setStopNumber(r.getId().getTravelStopNumber());
            s.setStationId(r.getStation().getId());
            s.setDistance(r.getDistance());
            s.setPrice(BigDecimal.valueOf(r.getPrice().doubleValue()));
            s.setArrivalTime(base.plus(r.getArrivalOffset() != null ? r.getArrivalOffset() : Duration.ZERO));
            s.setDepartureTime(base.plus(r.getDepartureOffset() != null ? r.getDepartureOffset() : Duration.ZERO));
            dto.getStops().add(s);
        }
        return dto;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void updateUser(Long id, String email, UserRole role) {
        User user = getUserById(id);
        user.setEmail(email);
        user.setRole(role);
        userRepository.save(user);
    }

    public List<User> searchUsers(String query) {
        if (query == null || query.isBlank()) {
            return userRepository.findAll();
        }
        return userRepository.findAll().stream()
                .filter(u -> u.getEmail().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

    @Transactional
    public void toggleUserEnabled(Long id) {
        User user = getUserById(id);
        user.setEnabled(!user.isEnabled());
    }

    @Transactional
    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(
                    "Nie można usunąć użytkownika: istnieją powiązane bilety lub rezerwacje.", e);
        }
    }

    @Transactional
    public void deleteTravel(Long id) {
        travelRepository.deleteById(id);
    }

    /**
     * Drops null entries, Spring "gap" placeholders, and rows without a chosen station
     * (e.g. after removing a table row without renumbering {@code stops[i]} indices).
     */
    private static List<StopFormDto> sanitizeStops(List<StopFormDto> raw) {
        if (raw == null) {
            return List.of();
        }
        return raw.stream()
                .filter(Objects::nonNull)
                .filter(s -> s.getStationId() != null)
                .toList();
    }
}
