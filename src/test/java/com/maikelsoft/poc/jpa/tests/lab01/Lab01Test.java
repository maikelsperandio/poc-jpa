package com.maikelsoft.poc.jpa.tests.lab01;

import com.maikelsoft.poc.jpa.domain.Address;
import com.maikelsoft.poc.jpa.domain.Employee;
import com.maikelsoft.poc.jpa.domain.Project;
import com.maikelsoft.poc.jpa.domain.ProjectStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Lab01Test {

    private static EntityManagerFactory emFactory;
    private static EntityManager em;

    @BeforeAll
    public static void setUp(){
        emFactory = Persistence.createEntityManagerFactory("poc-jpa");
        em = emFactory.createEntityManager();
    }

    @AfterAll
    public static void tearDown(){
        if (Objects.nonNull(emFactory))
            emFactory.close();

        if (Objects.nonNull(em))
            em.close();
    }

    @Test
    public void testEmployeePersistence() {
        em.getTransaction().begin();

        Address address = new Address("Rua de Santa Catarina", "Oporto", "4000-441");
        Employee employee = new Employee("John", "Doe", "john.doe@example.com");
        employee.setAddress(address);

        em.persist(employee);
        em.getTransaction().commit();

        assertNotNull(employee.getId());

        Employee found = em.find(Employee.class, employee.getId());
        assertEquals("John", found.getFirstName());
        assertEquals("Oporto", found.getAddress().getCity());
    }

    @Test
    public void testProjectPersistence() {
        em.getTransaction().begin();

        Project project = new Project("New Website", "Redesign company website", new BigDecimal("50000.00"), ProjectStatus.PLANNED);
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now().plusMonths(3));

        em.persist(project);
        em.getTransaction().commit();

        assertNotNull(project.getId());
        assertNotNull(project.getCreatedOn(), "CreatedOn should be set by @PrePersist");

        Project found = em.find(Project.class, project.getId());
        assertEquals("New Website", found.getName());
        assertEquals(ProjectStatus.PLANNED, found.getStatus());
    }
}
