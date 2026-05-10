package net.f1v.kolexbackend.repository;

import net.f1v.kolexbackend.entity.TravelRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TravelRouteRepository extends JpaRepository<TravelRoute, Long> {
    @Query("SELECT tr FROM TravelRoute tr WHERE tr.id.travelId = :travelId AND tr.id.travelStopNumber = :travelStopNumber")
    TravelRoute findByTravelIdAndTravelStopNumber(@Param("travelId") Long travelId, @Param("travelStopNumber") Integer travelStopNumber);
}
