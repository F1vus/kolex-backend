package net.f1v.kolexbackend.repository;

import net.f1v.kolexbackend.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    boolean existsByTravel_Id(Long travelId);

    @Query("SELECT t FROM Ticket t " +
            "JOIN FETCH t.profile p " +
            "JOIN FETCH t.travel tr " +
            "LEFT JOIN FETCH t.seat s " +
            "LEFT JOIN FETCH tr.routes route " +
            "WHERE p.user.id = :userId " +
            "AND t.status = net.f1v.kolexbackend.entity.states.TicketStatus.PAID " +
            "ORDER BY tr.departure DESC")
    List<Ticket> findPaidTicketsByUserId(@Param("userId") Long userId);

    Ticket getTicketByIdAndProfile_UserId(Long ticketId, Long userId);
}
