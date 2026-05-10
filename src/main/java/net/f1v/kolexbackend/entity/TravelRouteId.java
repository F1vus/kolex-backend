package net.f1v.kolexbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TravelRouteId implements Serializable {

    @Column(name = "travel_id")
    private Long travelId;

    @Column(name = "travel_stop_number")
    private Integer travelStopNumber;
}
