package com.maikelsoft.poc.jpa.tests.lab03;

import com.maikelsoft.poc.jpa.domain.Employee;
import com.maikelsoft.poc.jpa.identifiers.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Lab03IdentifiersTest {
    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeEach
    protected void setUp() {
        emf = Persistence.createEntityManagerFactory("poc-jpa");
        em = emf.createEntityManager();
    }

    @AfterEach
    protected void tearDown() {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }

//    @Test
    public void testSequenceGenerator() {
        em.getTransaction().begin();

        Subscription s1 = new Subscription("Basic", 9.99);
        Subscription s2 = new Subscription("Pro", 19.99);

        em.persist(s1);
        em.persist(s2);

        em.getTransaction().commit();

        System.out.println("Sub 1 ID: " + s1.getId());
        System.out.println("Sub 2 ID: " + s2.getId());

        assertNotNull(s1.getId());
        assertNotNull(s2.getId());
        assertNotEquals(s1.getId(), s2.getId());
    }

    @Test
    public void testMapsIdWithEmployeeProfile() {
        em.getTransaction().begin();

        Employee employee = new Employee("John", "Doe", "john.doe.profile@example.com");
        em.persist(employee); // ID generated (IDENTITY)

        EmployeeProfile profile = new EmployeeProfile(employee, "Software Engineer with 10 years experience");
        // No explicit ID set, should come from employee
        em.persist(profile);

        em.getTransaction().commit();

        Long empId = employee.getId();
        Long profileId = profile.getId();

        System.out.println("Employee ID: " + empId);
        System.out.println("Profile ID: " + profileId);

        assertNotNull(empId);
        assertEquals(empId, profileId, "Profile ID should match Employee ID");
    }

    @Test
    public void testCompositeKeyEmployeeSkill() {
        em.getTransaction().begin();

        Employee employee = new Employee("Jane", "Smith", "jane.smith.skill@example.com");
        em.persist(employee);

        Skill javaSkill = new Skill("Java");
        em.persist(javaSkill);

        EmployeeSkill employeeSkill = new EmployeeSkill(employee, javaSkill, 9);
        em.persist(employeeSkill);

        em.getTransaction().commit();

        em.clear(); // Detach to force fetch

        EmployeeSkillId id = new EmployeeSkillId(employee.getId(), javaSkill.getId());
        EmployeeSkill found = em.find(EmployeeSkill.class, id);

        assertNotNull(found);
        assertEquals(9, found.getProficiencyLevel());
        assertEquals("Java", found.getSkill().getName());
        assertEquals("Jane", found.getEmployee().getFirstName());
    }
}
