package org.example.employeeprojection.repository;

import org.example.employeeprojection.model.Employee;
import org.example.employeeprojection.projection.EmployeeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<EmployeeProjection> findAllBy();

    Optional<EmployeeProjection> findProjectionById(Long id);
}