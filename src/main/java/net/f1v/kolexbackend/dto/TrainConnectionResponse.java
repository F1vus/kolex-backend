package net.f1v.kolexbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainConnectionResponse {
    private Long travelId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Double price;
}