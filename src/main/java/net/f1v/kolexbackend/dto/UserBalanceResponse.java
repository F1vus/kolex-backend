package net.f1v.kolexbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class UserBalanceResponse {
    private BigDecimal balance;
}