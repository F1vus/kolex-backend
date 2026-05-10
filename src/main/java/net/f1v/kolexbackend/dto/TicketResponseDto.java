package net.f1v.kolexbackend.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketResponseDto {
    private  Long id;
    private Long travelId;
    private  String trainName;
    private  String profileName;
    private  String fromStationName;
    private  String toStationName;
    private  Integer seatNumber;
    private  String actualDeparture;
    private  String actualArrival;
    private  BigDecimal price;
    private  Integer travelStopNumberFrom;
    private  Integer travelStopNumberTo;
}
