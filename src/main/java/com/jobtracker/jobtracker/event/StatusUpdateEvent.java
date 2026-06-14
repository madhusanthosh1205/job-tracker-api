package com.jobtracker.jobtracker.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusUpdateEvent {
    private String userEmail;
    private String userName;
    private String company;
    private String role;
    private String oldStatus;
    private String newStatus;
}
