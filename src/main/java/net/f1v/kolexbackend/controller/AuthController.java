package net.f1v.kolexbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.f1v.kolexbackend.dto.AuthResponse;
import net.f1v.kolexbackend.dto.LoginRequest;
import net.f1v.kolexbackend.dto.RegisterRequest;
import net.f1v.kolexbackend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Register and login endpoints for the Android app (JWT)")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Register new account",
            description = "Creates a new user. Returns a JWT token immediately so the client can proceed without a separate login call"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already in use"),
            @ApiResponse(responseCode = "400", description = "Passwords do not match")
    })
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        log.info("POST /api/auth/register; email={}", request.getEmail());
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Authenticates existing user, returns JWT with refresh token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid email or password")
    })
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}