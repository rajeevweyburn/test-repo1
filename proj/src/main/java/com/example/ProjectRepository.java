package com.example;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Collection<Project> findByEmployerName(String name);
    
    Collection<Project> findByEmployerId(long id);
}