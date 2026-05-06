package net.f1v.kolexbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "station", schema = "backend")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "station_id")
    private Long id;

    @Column(name = "station_name", nullable = false, unique = true, length = 150)
    private String name;

    @Column(name = "station_city", nullable = false, length = 150)
    private String city;

    @OneToMany(mappedBy = "station")
    private List<TravelRoute> routes;
}