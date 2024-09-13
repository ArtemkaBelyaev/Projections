package org.example.employeeprojection.projection;

public interface EmployeeProjection {

    String getFirstName();

    String getLastName();

    default String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    String getPosition();

    String getDepartmentName();
}


