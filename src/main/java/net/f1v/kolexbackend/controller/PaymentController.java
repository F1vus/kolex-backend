package net.f1v.kolexbackend.controller;

import lombok.RequiredArgsConstructor;
import net.f1v.kolexbackend.config.jwtConfig.UserPrincipal;
import net.f1v.kolexbackend.dto.BuyRandomTicketRequest;
import net.f1v.kolexbackend.dto.BuyTicketRequest;
import net.f1v.kolexbackend.dto.BuyTicketResponse;
import net.f1v.kolexbackend.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/buy-ticket")
    public BuyTicketResponse buyTicket(@RequestBody BuyTicketRequest buyTicketRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return paymentService.purchaseTicket(
                buyTicketRequest.getReservationId(),
                userPrincipal.getId()
        );
    }

    @PostMapping("/purchase-random")
    @ResponseStatus(HttpStatus.CREATED)
    public BuyTicketResponse purchaseRandom(@RequestBody BuyRandomTicketRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return paymentService.purchaseRandomTicket(
                request.getTravelId(),
                userPrincipal.getId(),
                request.getProfileId(),
                request.getStartStop(),
                request.getEndStop()
        );
    }
}
