package net.f1v.kolexbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class BuyTicketResponse {
    private Long ticketId;
    private String message;
}
