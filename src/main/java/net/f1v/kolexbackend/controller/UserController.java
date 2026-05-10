package net.f1v.kolexbackend.controller;

import lombok.RequiredArgsConstructor;
import net.f1v.kolexbackend.config.jwtConfig.UserPrincipal;
import net.f1v.kolexbackend.dto.UserBalanceResponse;
import net.f1v.kolexbackend.entity.User;
import net.f1v.kolexbackend.error.exceptions.BusinessException;
import net.f1v.kolexbackend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/balance")
    public UserBalanceResponse getBalance(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new BusinessException(
                        "User not found", HttpStatus.NOT_FOUND));
        return new UserBalanceResponse(user.getBalance());
    }
}