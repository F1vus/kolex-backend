package net.f1v.kolexbackend.repository;

import net.f1v.kolexbackend.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
