package com.cookbook.repository;

import com.cookbook.model.Process;
import com.cookbook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {
    List<Process> findByCreatedBy(User createdBy);
    List<Process> findByCreatedById(Long userId);
    Optional<Process> findByIdAndCreatedById(Long id, Long userId);
    boolean existsByNameAndCreatedById(String name, Long userId);
}