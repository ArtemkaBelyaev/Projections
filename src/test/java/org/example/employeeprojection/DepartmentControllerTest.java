package org.example.employeeprojection;


import org.example.employeeprojection.controller.DepartmentController;
import org.example.employeeprojection.model.Department;
import org.example.employeeprojection.service.DepartmentService;
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

class DepartmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
    }

    @Test
    void testGetAllDepartments() throws Exception {
        Department department = new Department();
        when(departmentService.getAllDepartments()).thenReturn(List.of(department));

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(departmentService, times(1)).getAllDepartments();
    }

    @Test
    void testGetDepartmentById() throws Exception {
        Department department = new Department();
        when(departmentService.getDepartmentById(1L)).thenReturn(Optional.of(department));

        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(departmentService, times(1)).getDepartmentById(1L);
    }

    @Test
    void testCreateDepartment() throws Exception {
        Department department = new Department();
        when(departmentService.saveDepartment(any(Department.class))).thenReturn(department);

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"IT\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(departmentService, times(1)).saveDepartment(any(Department.class));
    }

    @Test
    void testUpdateDepartment() throws Exception {
        Department department = new Department();
        department.setId(1L);
        department.setName("IT");
        Mockito.when(departmentService.updateDepartment(anyLong(), any(Department.class))).thenReturn(Optional.of(department));
        mockMvc.perform(put("/api/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"IT\",\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("IT"));

    }

    @Test
    void testDeleteDepartment() throws Exception {
        Mockito.when(departmentService.deleteDepartment(1L)).thenReturn(true);
        mockMvc.perform(delete("/api/departments/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(departmentService).deleteDepartment(1L);
    }
}