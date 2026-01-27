package com.maikelsoft.poc.jpa.tests.lab02;

import com.maikelsoft.poc.jpa.domain.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Lab02SecondaryTableTest {

    @Test
    public void testSecondaryTable() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("poc-jpa");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Employee employee = new Employee("Rich", "Guy", "rich@example.com");
        employee.setSalary(new BigDecimal("100000.00"));
        em.persist(employee);

        em.getTransaction().commit();

        Long employeeId = employee.getId();
        em.close();

        em = emf.createEntityManager();
        Employee foundEmployee = em.find(Employee.class, employeeId);
        assertNotNull(foundEmployee);
        assertEquals(new BigDecimal("100000.00"), foundEmployee.getSalary());

        em.close();
        emf.close();
    }
}
