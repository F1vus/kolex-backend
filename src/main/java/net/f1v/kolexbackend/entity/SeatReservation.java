package net.f1v.kolexbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "seat_reservation", schema = "backend")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "seatReservation")
    @JoinColumn(name = "ticket_id", nullable = true)
    private List<Ticket> ticket;

    @Column(name = "start_stop_number", nullable = false)
    private Integer startStopNumber;

    @Column(name = "end_stop_number", nullable = false)
    private Integer endStopNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReservationStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}