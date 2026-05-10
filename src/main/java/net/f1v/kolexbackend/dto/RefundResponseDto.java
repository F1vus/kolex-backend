package net.f1v.kolexbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class RefundResponseDto {
    private Long ticketId;
    private BigDecimal refundAmount;
    private String message;
}
