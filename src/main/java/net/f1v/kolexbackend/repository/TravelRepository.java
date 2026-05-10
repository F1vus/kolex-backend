package net.f1v.kolexbackend.repository;

import net.f1v.kolexbackend.dto.TravelOptionDTO;
import net.f1v.kolexbackend.entity.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {

    @Query("SELECT new net.f1v.kolexbackend.dto.TravelOptionDTO(" +
            "t.id, " +
            "t.train, " +
            "sStart.name, " +
            "sEnd.name, " +
            "t.departure, " +
            "trStart.departureOffset, " +
            "trEnd.arrivalOffset, " +
            "(trEnd.price - trStart.price), " +
            "trStart.id.travelStopNumber," +
            "trEnd.id.travelStopNumber)"+
            "FROM Travel t " +
            "JOIN TravelRoute trStart ON t.id = trStart.id.travelId " +
            "JOIN TravelRoute trEnd ON t.id = trEnd.id.travelId " +
            "JOIN Station sStart ON trStart.station.id = sStart.id " +
            "JOIN Station sEnd ON trEnd.station.id = sEnd.id " +
            "WHERE sStart.id = :startStationId " +
            "AND sEnd.id = :endStationId " +
            "AND trStart.id.travelStopNumber < trEnd.id.travelStopNumber " +
            "AND (t.departure + trStart.departureOffset) >= :startTime " +
            "AND (t.departure + trStart.departureOffset) <= :endTime " +
            "ORDER BY (t.departure + trStart.departureOffset) ASC")
    List<TravelOptionDTO> findAvailableTravels(
            @Param("startStationId") Long startStationId,
            @Param("endStationId") Long endStationId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
