package com.maikelsoft.poc.jpa.tests.lab02;

import com.maikelsoft.poc.jpa.domain.Employee;
import com.maikelsoft.poc.jpa.domain.Project;
import com.maikelsoft.poc.jpa.domain.ProjectStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Lab02ManyToManyTest {

    @Test
    public void testManyToMany() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("poc-jpa");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Project project = new Project("AI Project", "Developing AI", new BigDecimal("100000"), ProjectStatus.PLANNED);
        Employee employee1 = new Employee("Alice", "Wonder", "alice@example.com");
        Employee employee2 = new Employee("Bob", "Builder", "bob@example.com");

        em.persist(employee1);
        em.persist(employee2);

        project.addEmployee(employee1);
        project.addEmployee(employee2);

        em.persist(project);
        em.getTransaction().commit();

        java.util.UUID projectId = project.getId();
        em.close();

        em = emf.createEntityManager();
        Project foundProject = em.find(Project.class, projectId);
        assertNotNull(foundProject);
        assertEquals(2, foundProject.getEmployees().size());

        Employee foundAlice = foundProject.getEmployees().stream().filter(e -> e.getFirstName().equals("Alice")).findFirst().orElse(null);
        assertNotNull(foundAlice);
        assertEquals(1, foundAlice.getProjects().size());

        em.close();
        emf.close();
    }

}
