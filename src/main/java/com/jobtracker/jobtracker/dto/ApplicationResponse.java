package com.jobtracker.jobtracker.dto;


import com.jobtracker.jobtracker.entity.ApplicationStatus;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ApplicationResponse {
    private Long id;
    private String company;
    private String role;
    private ApplicationStatus status;
    private LocalDate appliedDate;
    private String notes;
}
