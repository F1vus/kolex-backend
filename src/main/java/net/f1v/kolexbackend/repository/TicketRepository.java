package net.f1v.kolexbackend.repository;

import net.f1v.kolexbackend.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByProfile_User_Id(Long profileUserId);
}
