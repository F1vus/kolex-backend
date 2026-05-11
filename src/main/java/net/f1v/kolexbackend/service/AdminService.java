package net.f1v.kolexbackend.service;

import lombok.RequiredArgsConstructor;
import net.f1v.kolexbackend.dto.admin.StopFormDto;
import net.f1v.kolexbackend.dto.admin.TravelFormDto;
import net.f1v.kolexbackend.entity.*;
import net.f1v.kolexbackend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                    .orElseThrow(() -> new RuntimeException("Travel not found"));
            travel.getRoutes().clear();
        } else {
            travel = new Travel();
        }

        travel.setTrain(dto.getTrain());
        travel.setDeparture(dto.getDeparture());

        List<TravelRoute> routes = new ArrayList<>();
        for (StopFormDto stop : dto.getStops()) {
            Station station = stationRepository.findById(stop.getStationId())
                    .orElseThrow(() -> new RuntimeException("Station not found"));

            TravelRouteId routeId = new TravelRouteId(
                    travel.getId() != null ? travel.getId() : 0L,
                    stop.getStopNumber());

            TravelRoute route = new TravelRoute();
            route.setId(routeId);
            route.setTravel(travel);
            route.setStation(station);
            route.setDistance(stop.getDistance());
            route.setPrice(stop.getPrice());
            route.setArrivalOffset(Duration.parse(stop.getArrivalOffset()));
            route.setDepartureOffset(Duration.parse(stop.getDepartureOffset()));
            routes.add(route);
        }

        travel.setRoutes(routes);
        travelRepository.save(travel);
    }

    @Transactional
    public void deleteTravel(Long id) {
        travelRepository.deleteById(id);
    }

    public TravelFormDto getTravelForEdit(Long id) {
        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Travel not found"));

        TravelFormDto dto = new TravelFormDto();
        dto.setId(travel.getId());
        dto.setTrain(travel.getTrain());
        dto.setDeparture(travel.getDeparture());

        if (travel.getRoutes() != null) {
            for (TravelRoute r : travel.getRoutes()) {
                StopFormDto stop = new StopFormDto();
                stop.setStationId(r.getStation().getId());
                stop.setStopNumber(r.getId().getTravelStopNumber());
                stop.setDistance(r.getDistance());
                stop.setPrice(r.getPrice());
                stop.setArrivalOffset(r.getArrivalOffset().toString());
                stop.setDepartureOffset(r.getDepartureOffset().toString());
                dto.getStops().add(stop);
            }
        }
        return dto;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> searchUsers(String query) {
        if (query == null || query.isBlank()) return getAllUsers();
        return userRepository.findAll().stream()
                .filter(u -> u.getEmail().toLowerCase()
                        .contains(query.toLowerCase()))
                .toList();
    }

    @Transactional
    public void toggleUserEnabled(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}