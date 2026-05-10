package net.f1v.kolexbackend.service;

import lombok.RequiredArgsConstructor;
import net.f1v.kolexbackend.entity.Ticket;
import net.f1v.kolexbackend.entity.states.TicketStatus;
import net.f1v.kolexbackend.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;

    public List<Ticket> getAllTickets(Long id) {
        return ticketRepository.findByUserIdWithDetails(id, TicketStatus.PAID);
    }
}
