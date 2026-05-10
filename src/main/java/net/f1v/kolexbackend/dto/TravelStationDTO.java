package net.f1v.kolexbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

@Getter
@AllArgsConstructor
public class TravelStationDTO {
    private Integer stopNumber;
    private String stationName;
    private Duration arrivalOffset;
    private Duration departureOffset;
    private Integer distance;
}