package com.maikelsoft.poc.jpa.tests.lab02;

import com.maikelsoft.poc.jpa.domain.Employee;
import com.maikelsoft.poc.jpa.domain.Project;
import com.maikelsoft.poc.jpa.domain.ProjectDetails;
import com.maikelsoft.poc.jpa.domain.ProjectStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Lab02CustomFiltersRelation {

    @Test
    public void testCustomFilterRelation(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("poc-jpa");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Project project01 = new Project("Secret Project", "Top Secret", new BigDecimal("500000"), ProjectStatus.PLANNED);
        ProjectDetails details = new ProjectDetails("http://git/secret", "Bond");
        project01.setProjectDetails(details);

        Project project02 = new Project("Secret Project", "Top Secret", new BigDecimal("500000"), ProjectStatus.IN_PROGRESS);
        ProjectDetails details02 = new ProjectDetails("http://git/secret", "Bond");
        project02.setProjectDetails(details02);

        em.persist(project01); // Should cascade persist to details
        em.persist(project02); // Should cascade persist to details

        Employee mane = new Employee("Mane", "Pipoca", "mane@example.com");
        mane.setProjects(Set.of(project01, project02));

        em.persist(mane);
        em.getTransaction().commit();

        Long maneId = mane.getId();

        em.close();
        em = emf.createEntityManager();
        Employee mane02 = em.find(Employee.class, maneId);
        assertNotNull(mane02);

        em.close();
        emf.close();
    }
}
