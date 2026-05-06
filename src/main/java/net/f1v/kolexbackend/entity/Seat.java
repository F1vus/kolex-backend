package net.f1v.kolexbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seat", schema = "backend",
        uniqueConstraints = {
                @UniqueConstraint(name = "un_seat", columnNames = {"travel_id", "seat_number"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id", nullable = false)
    private Travel travel;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;
}