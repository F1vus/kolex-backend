package net.f1v.kolexbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchTrainRequest {
    private Long fromStationId;
    private Long toStationId;
    private LocalDate departureDate;
}