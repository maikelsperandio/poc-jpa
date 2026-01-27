package com.maikelsoft.poc.jpa.tests.lab02;

import com.maikelsoft.poc.jpa.domain.Department;
import com.maikelsoft.poc.jpa.domain.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Lab02DepartmentTest {

    @Test
    public void testDepartment() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("poc-jpa");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Department department = new Department("IT");
        em.persist(department);

        Employee employee = new Employee("Dave", "Developer", "dave@example.com");
        employee.setDepartment(department);
        em.persist(employee);

        em.getTransaction().commit();

        Long departmentId = department.getId();
        em.close();

        em = emf.createEntityManager();
        Department foundDepartment = em.find(Department.class, departmentId);
        assertNotNull(foundDepartment);
        assertEquals("IT", foundDepartment.getName());
        assertEquals(1, foundDepartment.getEmployees().size());
        assertEquals("Dave", foundDepartment.getEmployees().get(0).getFirstName());

        em.close();
        emf.close();
    }
}
