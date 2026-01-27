package com.maikelsoft.poc.jpa.tests.lab02;

import com.maikelsoft.poc.jpa.domain.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Lab02InheritanceTest {

    @Test
    public void testInheritance() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("poc-jpa");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Project project = new Project("Tasks Project", "Task Tracking", new BigDecimal("500"), ProjectStatus.IN_PROGRESS);

        CodingTask codingTask = new CodingTask("Implement Feature X", LocalDate.now().plusDays(5), "Java");
        BusinessTask managementTask = new BusinessTask("Sprint Planning", LocalDate.now().plusDays(1), "zoom.us/j/123");

        project.addTask(codingTask);
        project.addTask(managementTask);

        em.persist(project); // Cascades to tasks
        em.getTransaction().commit();

        java.util.UUID projectId = project.getId();
        em.close();

        em = emf.createEntityManager();
        Project foundProject = em.find(Project.class, projectId);
        assertNotNull(foundProject);
        assertEquals(2, foundProject.getTasks().size());

        // Polymorphic query
        List<Task> allTasks = em.createQuery("SELECT t FROM Task t", Task.class).getResultList();
        assertEquals(2, allTasks.size());

        Task t1 = allTasks.stream().filter(t -> t instanceof CodingTask).findFirst().orElse(null);
        assertTrue(t1 instanceof CodingTask);
        assertEquals("Java", ((CodingTask) t1).getLanguage());

        Task t2 = allTasks.stream().filter(t -> t instanceof BusinessTask).findFirst().orElse(null);
        assertTrue(t2 instanceof BusinessTask);
        assertEquals("zoom.us/j/123", ((BusinessTask) t2).getMeetingUrl());

        em.close();
        emf.close();
    }

}
