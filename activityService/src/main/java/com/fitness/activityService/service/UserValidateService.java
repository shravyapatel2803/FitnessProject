package com.fitness.activityService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class UserValidateService {

    private final WebClient userServiceWebClient;

    public boolean validateUser(String userId) {
        try {
            // Makes a call to User Service to check if user exists
            return Boolean.TRUE.equals(userServiceWebClient
                    .get()
                    .uri("/api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block());
        } catch (WebClientResponseException e) {
            // Properly handle 404 Not Found from User Service
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                throw new RuntimeException("User not found");
            }
            throw e;
        } catch (Exception e) {
            // Handle connection errors or other issues
            throw new RuntimeException("User validation service unavailable", e);
        }
    }
}