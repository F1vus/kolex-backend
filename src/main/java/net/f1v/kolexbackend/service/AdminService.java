package net.f1v.kolexbackend.service;

import lombok.RequiredArgsConstructor;
import net.f1v.kolexbackend.dto.admin.StopFormDto;
import net.f1v.kolexbackend.dto.admin.TravelFormDto;
import net.f1v.kolexbackend.entity.*;
import net.f1v.kolexbackend.entity.states.UserRole;
import net.f1v.kolexbackend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final TravelRepository travelRepository;
    private final StationRepository stationRepository;
    private final UserRepository userRepository;

    public List<Travel> getAllTravels() {
        return travelRepository.findAll();
    }

    @Transactional
    public void saveTravel(TravelFormDto dto) {
        Travel travel;
        if (dto.getId() != null) {
            travel = travelRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Nie znaleziono podróży"));
            travel.getRoutes().clear();
        } else {
            travel = new Travel();
            travel.setRoutes(new ArrayList<>());
        }

        travel.setTrain(dto.getTrain());
        travel.setDeparture(dto.getDeparture());

        Duration maxDuration = Duration.ZERO;

        for (StopFormDto stopDto : dto.getStops()) {
            Station station = stationRepository.findById(stopDto.getStationId())
                    .orElseThrow(() -> new RuntimeException("Nie znaleziono stacji"));

            TravelRoute route = new TravelRoute();
            route.setTravel(travel);
            route.setStation(station);
            route.setDistance(stopDto.getDistance());
            route.setPrice(stopDto.getPrice());

            Duration arr = Duration.ofMinutes(stopDto.getArrivalMinutes());
            Duration dep = Duration.ofMinutes(stopDto.getDepartureMinutes());

            route.setArrivalOffset(arr);
            route.setDepartureOffset(dep);

            if (arr.compareTo(maxDuration) > 0) maxDuration = arr;
            if (dep.compareTo(maxDuration) > 0) maxDuration = dep;

            travel.getRoutes().add(route);
        }

        travel.setTravelDuration(maxDuration);

        travelRepository.save(travel);
    }

    public TravelFormDto getTravelForEdit(Long id) {
        Travel t = travelRepository.findById(id).orElseThrow();
        TravelFormDto dto = new TravelFormDto();
        dto.setId(t.getId());
        dto.setTrain(t.getTrain());
        dto.setDeparture(t.getDeparture());

        for (TravelRoute r : t.getRoutes()) {
            StopFormDto s = new StopFormDto();
            s.setStationId(r.getStation().getId());
            s.setDistance(r.getDistance());
            s.setPrice(BigDecimal.valueOf(r.getPrice().doubleValue()));
            s.setArrivalMinutes((int) r.getArrivalOffset().toMinutes());
            s.setDepartureMinutes((int) r.getDepartureOffset().toMinutes());
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
        if (query == null || query.isBlank()) return userRepository.findAll();
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
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteTravel(Long id) {
        travelRepository.deleteById(id);
    }
}