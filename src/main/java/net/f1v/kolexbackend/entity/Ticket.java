package net.f1v.kolexbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket", schema = "backend")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @Column(name = "departure_date", nullable = false)
    private LocalDateTime departureDate;

    @Column(name = "ticket_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id", nullable = false)
    private Travel travel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(name = "start_stop_number", nullable = false)
    @Min(1)
    private Integer startStopNumber;

    @Column(name = "end_stop_number", nullable = false)
    private Integer endStopNumber;

    @Column(name = "ticket_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    private SeatReservation seatReservation;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}