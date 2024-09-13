package org.example.employeeprojection;

import org.example.employeeprojection.controller.EmployeeController;
import org.example.employeeprojection.model.Employee;
import org.example.employeeprojection.projection.EmployeeProjection;
import org.example.employeeprojection.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    void testGetAllEmployees() throws Exception {
        EmployeeProjection employeeProjection = new EmployeeProjection() {
            @Override
            public String getFirstName() {
                return "John";
            }

            @Override
            public String getLastName() {
                return "Doe";
            }

            @Override
            public String getPosition() {
                return "Developer";
            }

            @Override
            public String getDepartmentName() {
                return "IT";
            }
        };

        when(employeeService.getAllEmployees()).thenReturn(List.of(employeeProjection));
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].fullName").value("John Doe"))
                .andExpect(jsonPath("$[0].position").value("Developer"))
                .andExpect(jsonPath("$[0].departmentName").value("IT"));
        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void testGetEmployeeById() throws Exception {
        EmployeeProjection projection = new EmployeeProjection() {
            @Override
            public String getFirstName() {
                return "Jane";
            }

            @Override
            public String getLastName() {
                return "Doe";
            }

            @Override
            public String getFullName() {
                return "Jane Doe";
            }

            @Override
            public String getPosition() {
                return "Manager";
            }

            @Override
            public String getDepartmentName() {
                return "HR";
            }
        };

        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(projection));
        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fullName").value("Jane Doe"))
                .andExpect(jsonPath("$.position").value("Manager"))
                .andExpect(jsonPath("$.departmentName").value("HR"));

        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    void testCreateEmployee() throws Exception {
        Employee employee = new Employee();

        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(employee);
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"position\":\"Developer\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(employeeService, times(1)).saveEmployee(any(Employee.class));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setPosition("Developer");

        Mockito.when(employeeService.updateEmployee(anyLong(), any(Employee.class))).thenReturn(Optional.of(employee));
        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"position\":\"Developer\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.position").value("Developer"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        Mockito.when(employeeService.deleteEmployee(1L)).thenReturn(true);
        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployee(1L);
    }
}