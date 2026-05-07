package net.f1v.kolexbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.f1v.kolexbackend.dto.StationResponse;
import net.f1v.kolexbackend.service.MetadataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/metadata")
@RequiredArgsConstructor
public class MetadataController {

    private final MetadataService metadataService;

    @GetMapping("/stations")
    @ApiResponse(responseCode = "200", description = "List of stations returned")
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT")
    public ResponseEntity<List<StationResponse>> getAllStations() {
        return ResponseEntity.ok(metadataService.getAllStations());
    }
}
