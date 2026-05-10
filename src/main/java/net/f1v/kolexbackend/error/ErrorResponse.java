package net.f1v.kolexbackend.error;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ErrorResponse {
    private int status;
    private String message;
    private long timestamp;
}