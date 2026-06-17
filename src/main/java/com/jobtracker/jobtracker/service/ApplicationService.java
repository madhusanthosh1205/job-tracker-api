
package com.jobtracker.jobtracker.service;

import com.jobtracker.jobtracker.dto.ApplicationRequest;
import com.jobtracker.jobtracker.dto.ApplicationResponse;
import com.jobtracker.jobtracker.entity.ApplicationStatus;
import com.jobtracker.jobtracker.entity.JobApplication;
import com.jobtracker.jobtracker.entity.User;
import com.jobtracker.jobtracker.repository.JobApplicationRepository;
import com.jobtracker.jobtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    // Helper: get User from email
    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Helper: convert Entity → Response DTO
    private ApplicationResponse toResponse(JobApplication app) {
        ApplicationResponse response = new ApplicationResponse();
        response.setId(app.getId());
        response.setCompany(app.getCompany());
        response.setRole(app.getRole());
        response.setStatus(app.getStatus());
        response.setAppliedDate(app.getAppliedDate());
        response.setNotes(app.getNotes());
        return response;
    }

    // CREATE application
    public ApplicationResponse create(ApplicationRequest request, String email) {
        User user = getUser(email);

        JobApplication app = new JobApplication();
        app.setUser(user);
        app.setCompany(request.getCompany());
        app.setRole(request.getRole());
        app.setStatus(ApplicationStatus.APPLIED);
        app.setAppliedDate(request.getAppliedDate());
        app.setNotes(request.getNotes());

        return toResponse(applicationRepository.save(app));
    }

    // GET ALL applications
    public List<ApplicationResponse> getAll(String email) {
        User user = getUser(email);
        return applicationRepository.findByUser(user)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // GET ONE application
    public ApplicationResponse getById(Long id, String email) {
        User user = getUser(email);
        JobApplication app = applicationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        return toResponse(app);
    }

    // UPDATE status + send email directly
    public ApplicationResponse updateStatus(Long id, String newStatus, String email) {
        User user = getUser(email);
        JobApplication app = applicationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        String oldStatus = app.getStatus().name();
        app.setStatus(ApplicationStatus.valueOf(newStatus.toUpperCase()));
        ApplicationResponse response = toResponse(applicationRepository.save(app));

        // Send email directly (async - runs in background)
        emailService.sendStatusUpdateEmail(
                user.getEmail(),
                user.getName(),
                app.getCompany(),
                app.getRole(),
                oldStatus,
                newStatus.toUpperCase()
        );

        return response;
    }

    // DELETE application
    public String delete(Long id, String email) {
        User user = getUser(email);
        JobApplication app = applicationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        applicationRepository.delete(app);
        return "Application deleted successfully";
    }
}