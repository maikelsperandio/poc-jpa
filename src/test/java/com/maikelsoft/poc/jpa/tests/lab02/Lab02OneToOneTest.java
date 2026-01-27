package com.maikelsoft.poc.jpa.tests.lab02;

import com.maikelsoft.poc.jpa.domain.AccessCard;
import com.maikelsoft.poc.jpa.domain.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Lab02OneToOneTest {

    @Test
    public void testOneToOne() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("poc-jpa");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Employee employee = new Employee("Bastiao", "Oliveira", "bastiao.oliveira@example.com");
        AccessCard card = new AccessCard(LocalDate.now(), true);
        employee.setAccessCard(card);

        em.persist(employee);
        em.getTransaction().commit();

        Long employeeId = employee.getId();
        em.close();

        em = emf.createEntityManager();
        Employee foundEmployee = em.find(Employee.class, employeeId);
        assertNotNull(foundEmployee);
        assertNotNull(foundEmployee.getAccessCard());
        assertTrue(foundEmployee.getAccessCard().isActive());

        em.close();
        emf.close();
    }
}
