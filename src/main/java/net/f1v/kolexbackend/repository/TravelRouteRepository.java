package net.f1v.kolexbackend.repository;

import net.f1v.kolexbackend.entity.TravelRoute;
import net.f1v.kolexbackend.entity.TravelRouteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRouteRepository extends JpaRepository<TravelRoute, TravelRouteId> {
    List<TravelRoute> findByTravelIdOrderByIdTravelStopNumberAsc(Long travelId);
}