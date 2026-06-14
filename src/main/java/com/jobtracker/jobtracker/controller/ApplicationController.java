package com.jobtracker.jobtracker.controller;

import com.jobtracker.jobtracker.dto.ApplicationRequest;
import com.jobtracker.jobtracker.dto.ApplicationResponse;
import com.jobtracker.jobtracker.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // Authentication object gives us the logged-in user's email from JWT

    @PostMapping
    public ResponseEntity<ApplicationResponse> create(
            @Valid @RequestBody ApplicationRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(applicationService.create(request, email));
    }

    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> getAll(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(applicationService.getAll(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getById(
            @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(applicationService.getById(id, email));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(applicationService.updateStatus(id, status, email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(applicationService.delete(id, email));
    }
}
