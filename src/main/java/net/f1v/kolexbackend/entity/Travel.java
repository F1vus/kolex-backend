package net.f1v.kolexbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "travel", schema = "backend")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id")
    private Long id;

    @Column(name = "travel_departure", nullable = false)
    private LocalDateTime departure;

    @Column(name = "travel_duration", columnDefinition = "INTERVAL")
    @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
    private Duration travelDuration;

    @Column(name = "travel_train", nullable = false, length = 50)
    private String train;

    @BatchSize(size = 32)
    @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL)
    private List<TravelRoute> routes;

    @BatchSize(size = 32)
    @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats;
}