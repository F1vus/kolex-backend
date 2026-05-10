package net.f1v.kolexbackend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class TopUpRequest {
    private BigDecimal amount;
}