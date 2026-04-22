package net.f1v.kolexbackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}