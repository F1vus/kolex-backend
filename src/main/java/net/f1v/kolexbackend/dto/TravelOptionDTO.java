package net.f1v.kolexbackend.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter
public class TravelOptionDTO {
    private final Long travelId;
    private final String trainName;
    private final String fromStationName;
    private final String toStationName;
    private final LocalDateTime actualDeparture;
    private final LocalDateTime actualArrival;
    private final String duration;
    private final BigDecimal price;
    private final Integer travelStopNumberFrom;
    private final Integer travelStopNumberTo;

    public TravelOptionDTO(
            Long id,
            String train,
            String fromStation,
            String toStation,
            LocalDateTime baseDeparture,
            Duration departureOffset,
            Duration arrivalOffset,
            BigDecimal price,
            Integer travelStoNumberFrom,
            Integer travelStoNumberTo
    ) {
        this.travelId = id;
        this.trainName = train;
        this.fromStationName = fromStation;
        this.toStationName = toStation;
        this.price = price;
        this.travelStopNumberFrom = travelStoNumberFrom;
        this.travelStopNumberTo = travelStoNumberTo;

        this.actualDeparture = baseDeparture.plus(departureOffset);
        this.actualArrival = baseDeparture.plus(arrivalOffset);

        Duration diff = Duration.between(actualDeparture, actualArrival);
        this.duration = String.format("%dh %dm", diff.toHours(), diff.toMinutesPart());
    }
}