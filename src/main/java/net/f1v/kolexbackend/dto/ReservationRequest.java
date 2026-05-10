package net.f1v.kolexbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationRequest {
    private Long seatId;
    private Long profileId;
    private Integer startStopNumber;
    private Integer endStopNumber;
}
