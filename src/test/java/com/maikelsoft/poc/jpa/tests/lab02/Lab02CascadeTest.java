package com.maikelsoft.poc.jpa.tests.lab02;

import com.maikelsoft.poc.jpa.domain.Project;
import com.maikelsoft.poc.jpa.domain.ProjectDetails;
import com.maikelsoft.poc.jpa.domain.ProjectStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class Lab02CascadeTest {

    @Test
    public void testCascadeAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("poc-jpa");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Project project = new Project("Secret Project", "Top Secret", new BigDecimal("500000"), ProjectStatus.PLANNED);
        ProjectDetails details = new ProjectDetails("http://git/secret", "Bond");
        project.setProjectDetails(details);
        // details.setProject(project); // inverse side not strictly needed for persistence of relation if owner is Project

        em.persist(project); // Should cascade persist to details
        em.getTransaction().commit();

        java.util.UUID projectId = project.getId();
        Long detailsId = details.getId();
        em.close();

        // Verify persistence
        em = emf.createEntityManager();
        Project foundProject = em.find(Project.class, projectId);
        assertNotNull(foundProject);
        assertNotNull(foundProject.getProjectDetails());
        assertEquals("Bond", foundProject.getProjectDetails().getManager());
        em.close();

        // Test Cascade Remove
        em = emf.createEntityManager();
        em.getTransaction().begin();
        foundProject = em.find(Project.class, projectId);
        em.remove(foundProject); // Should cascade remove to details
        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        assertNull(em.find(Project.class, projectId));
        assertNull(em.find(ProjectDetails.class, detailsId));
        em.close();
        emf.close();
    }

}
