package net.f1v.kolexbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.f1v.kolexbackend.dto.ProfileRequest;
import net.f1v.kolexbackend.dto.ProfileResponse;
import net.f1v.kolexbackend.entity.Profile;
import net.f1v.kolexbackend.entity.User;
import net.f1v.kolexbackend.repository.ProfileRepository;
import net.f1v.kolexbackend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProfileResponse createProfile(ProfileRequest request) {
        User user = currentUser();

        if (profileRepository.existsByUserId(user.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Profile already exists");
        }

        Profile profile = Profile.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .user(user)
                .build();

        profileRepository.save(profile);

        log.info("Created profile id={} for user id={}", profile.getId(), user.getId());

        return toResponse(profile);
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfile() {
        User user = currentUser();

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));

        return toResponse(profile);
    }

    @Transactional
    public ProfileResponse updateProfile(ProfileRequest request) {
        User user = currentUser();

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));

        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());

        log.info("Updated profile id={} for user id={}", profile.getId(), user.getId());

        return toResponse(profile);
    }

    @Transactional
    public void deleteProfile() {
        User user = currentUser();

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));

        profileRepository.delete(profile);

        log.info("Deleted profile for user id={}", user.getId());
    }

    private User currentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        String email = auth.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private ProfileResponse toResponse(Profile profile) {
        return ProfileResponse.builder()
                .profileId(profile.getId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .createdAt(profile.getCreatedAt())
                .userId(profile.getUser().getId())
                .build();
    }
}