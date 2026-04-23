package net.f1v.kolexbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.f1v.kolexbackend.dto.ProfileRequest;
import net.f1v.kolexbackend.entity.User;
import net.f1v.kolexbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String token;

    @BeforeEach
    void setup() throws Exception {
        userRepository.deleteAll();

        User user = User.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("Password123!"))
                .build();

        userRepository.save(user);

        String loginJson = """
                {
                  "email": "test@test.com",
                  "password": "Password123!"
                }
                """;

        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        token = response.replaceAll(".*\"token\":\"([^\"]+)\".*", "$1");
    }

    @Test
    void fullProfileFlow() throws Exception {

        ProfileRequest createRequest = ProfileRequest.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .build();

        mockMvc.perform(post("/api/profile")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Jan"));

        mockMvc.perform(get("/api/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Kowalski"));

        ProfileRequest updateRequest = ProfileRequest.builder()
                .firstName("Jan")
                .lastName("Nowak")
                .build();

        mockMvc.perform(put("/api/profile/change-profile")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Nowak"));

        mockMvc.perform(delete("/api/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}