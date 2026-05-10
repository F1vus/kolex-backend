package net.f1v.kolexbackend.repository;

import net.f1v.kolexbackend.dto.TravelStationDTO;
import net.f1v.kolexbackend.entity.TravelRoute;
import net.f1v.kolexbackend.entity.TravelRouteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRouteRepository extends JpaRepository<TravelRoute, TravelRouteId> {
    @Query("SELECT new net.f1v.kolexbackend.dto.TravelStationDTO(" +
            "tr.id.travelStopNumber, s.name, tr.arrivalOffset, tr.departureOffset, tr.distance) " +
            "FROM TravelRoute tr " +
            "JOIN tr.station s " +
            "WHERE tr.id.travelId = :travelId " +
            "ORDER BY tr.id.travelStopNumber ASC")
    List<TravelStationDTO> findAllStationsByTravelId(@Param("travelId") Long travelId);

    @Query("SELECT tr FROM TravelRoute tr WHERE tr.id.travelId = :travelId AND tr.id.travelStopNumber = :travelStopNumber")
    TravelRoute findByTravelIdAndTravelStopNumber(@Param("travelId") Long travelId, @Param("travelStopNumber") Integer travelStopNumber);
}