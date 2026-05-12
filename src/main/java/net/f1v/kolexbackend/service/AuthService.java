package net.f1v.kolexbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.f1v.kolexbackend.config.jwtConfig.JwtTokenProvider;
import net.f1v.kolexbackend.config.jwtConfig.UserPrincipal;
import net.f1v.kolexbackend.dto.AuthResponse;
import net.f1v.kolexbackend.dto.LoginRequest;
import net.f1v.kolexbackend.dto.RegisterRequest;
import net.f1v.kolexbackend.entity.User;
import net.f1v.kolexbackend.entity.states.UserRole;
import net.f1v.kolexbackend.error.exceptions.EmailAlreadyExistsException;
import net.f1v.kolexbackend.error.exceptions.InvalidCredentialsException;
import net.f1v.kolexbackend.error.exceptions.PasswordsDoNotMatchException;
import net.f1v.kolexbackend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Register email={}", request.getEmail());

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException("Passwords do not match");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use: " + request.getEmail());
        }

        userRepository.save(
                User.builder()
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(UserRole.USER)
                        .enabled(true)
                        .build()
        );

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        log.info("Register OK; email={}", request.getEmail());
        return buildResponse(authentication, request.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        log.info("Login email={}", request.getEmail());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            log.info("Login OK email={}", request.getEmail());
            return buildResponse(authentication, request.getEmail());

        } catch (BadCredentialsException e) {
            log.warn("Login failed email={}", request.getEmail());
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

    private AuthResponse buildResponse(Authentication authentication, String email) {
        String token = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(email);

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .type("Bearer")
                .email(email)
                .userId(principal.getId())
                .build();
    }
}