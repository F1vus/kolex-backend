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

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime departure;

    private List<StopFormDto> stops = new ArrayList<>();
}