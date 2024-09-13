package org.example.employeeprojection.service;

import org.example.employeeprojection.model.Employee;
import org.example.employeeprojection.projection.EmployeeProjection;
import org.example.employeeprojection.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<EmployeeProjection> getAllEmployees() {
        return employeeRepository.findAllBy();
    }

    public Optional<EmployeeProjection> getEmployeeById(Long id) {
        return employeeRepository.findProjectionById(id);
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Optional<Employee> updateEmployee(Long id, Employee updatedEmployee) {
        return employeeRepository.findById(id).map(existingEmployee -> {
            existingEmployee.setFirstName(updatedEmployee.getFirstName());
            existingEmployee.setLastName(updatedEmployee.getLastName());
            existingEmployee.setPosition(updatedEmployee.getPosition());
            existingEmployee.setSalary(updatedEmployee.getSalary());
            existingEmployee.setDepartment(updatedEmployee.getDepartment());
            return employeeRepository.save(existingEmployee);
        });
    }

    public boolean deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}