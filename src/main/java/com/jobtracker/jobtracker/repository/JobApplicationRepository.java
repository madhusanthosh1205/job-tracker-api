package com.jobtracker.jobtracker.repository;



import com.jobtracker.jobtracker.entity.JobApplication;
import com.jobtracker.jobtracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    // Get all applications belonging to a specific user
    List<JobApplication> findByUser(User user);

    // Get one application by id AND user (so users can't see each other's data)
    Optional<JobApplication> findByIdAndUser(Long id, User user);
}
