package net.f1v.kolexbackend.dto.admin;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TravelFormDto {
    private Long id;
    private String train;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime departure;

    /** Number of seats (1 … N) for this travel. */
    private Integer seatCount;

    private List<StopFormDto> stops = new ArrayList<>();
}