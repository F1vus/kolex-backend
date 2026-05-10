package net.f1v.kolexbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.f1v.kolexbackend.dto.StationResponse;
import net.f1v.kolexbackend.repository.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetadataService {

    private final StationRepository stationRepository;

    @Transactional
    public List<StationResponse> getAllStations() {
        return stationRepository.findAll()
                .stream()
                .map(station -> StationResponse.builder()
                        .id(station.getId())
                        .name(station.getName())
                        .city(station.getCity())
                        .build())
                .toList();
    }
}

