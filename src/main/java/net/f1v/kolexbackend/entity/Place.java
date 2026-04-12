package net.f1v.kolexbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "places", schema = "backend",
        uniqueConstraints = {
                @UniqueConstraint(name = "un_place",
                        columnNames = {"travel_id", "place_number"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "travel_id", nullable = false)
    private Travel travel;

    @Column(name = "place_number", nullable = false)
    private Integer placeNumber;
}
