package net.f1v.kolexbackend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {

    private Long profileId;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private Long userId;
}