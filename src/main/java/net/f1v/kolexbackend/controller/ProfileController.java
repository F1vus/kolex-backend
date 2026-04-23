package net.f1v.kolexbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.f1v.kolexbackend.dto.ProfileRequest;
import net.f1v.kolexbackend.dto.ProfileResponse;
import net.f1v.kolexbackend.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Profile", description = "Profile management endpoints")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    @Operation(summary = "Create profile", description = "Creates a profile for the authenticated user")
    public ResponseEntity<ProfileResponse> createProfile(
            @Valid @RequestBody ProfileRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(profileService.createProfile(request));
    }

    @GetMapping
    @Operation(summary = "Get current user profiles", description = "Retrieves profiles of the authenticated user")
    public ResponseEntity<List<ProfileResponse>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllMyProfiles());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update profile", description = "Updates provided profile of the authenticated user")
    public ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody ProfileRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete profile", description = "Deletes profile of the authenticated user")
    public void deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
    }
}