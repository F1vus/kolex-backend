package net.f1v.kolexbackend.dto.admin;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Data
public class StopFormDto {
    private Long stationId;
    private Integer stopNumber;
    private Integer distance;
    private BigDecimal price;
    private Integer arrivalMinutes;
    private Integer departureMinutes;
}