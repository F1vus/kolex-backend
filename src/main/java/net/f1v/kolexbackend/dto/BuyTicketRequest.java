package net.f1v.kolexbackend.dto;

import lombok.Data;

@Data
public class BuyTicketRequest {
    private Long userId;
    private Long reservationId;
}
