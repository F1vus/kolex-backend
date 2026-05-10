package net.f1v.kolexbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatStatusDTO {
    private Long seatId;
    private int seatNumber;
    private boolean isAvailable;
}