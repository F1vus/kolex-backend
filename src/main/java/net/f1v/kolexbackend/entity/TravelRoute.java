package net.f1v.kolexbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Duration;

@Entity
@Table(name = "travel_route", schema = "backend",
        uniqueConstraints = {
                @UniqueConstraint(name = "un_travel_route_station",
                        columnNames = {"travel_id", "travel_station_stop_id"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelRoute {

    @EmbeddedId
    private TravelRouteId id;

    @ManyToOne
    @MapsId("travelId")
    @JoinColumn(name = "travel_id", nullable = false)
    private Travel travel;

    @ManyToOne
    @JoinColumn(name = "travel_station_stop_id", nullable = false)
    private Station station;

    @Column(name = "travel_distance", nullable = false)
    @Min(0)
    private Integer distance;

    @Column(name = "travel_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "arrival_offset", columnDefinition = "INTERVAL")
    @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
    private Duration arrivalOffset;

    @Column(name = "departure_offset", columnDefinition = "INTERVAL")
    @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
    private Duration departureOffset;
}