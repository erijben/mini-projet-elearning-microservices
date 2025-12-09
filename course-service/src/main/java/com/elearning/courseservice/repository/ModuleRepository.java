package com.elearning.courseservice.repository;

import com.elearning.courseservice.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<Module, Long> {
}
