package com.maikelsoft.poc.jpa.tests.lab04;

import com.maikelsoft.poc.jpa.domain.Department;
import com.maikelsoft.poc.jpa.domain.Employee;
import com.maikelsoft.poc.jpa.domain.Project;
import com.maikelsoft.poc.jpa.dto.EmployeeDTO;
import jakarta.persistence.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Lab04ReadOperationsTest {

    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("poc-jpa");
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    @AfterEach
    public void tearDown() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        if (em.isOpen()) {
            em.close();
        }
        emf.close();
    }

    @Test
    public void testFindById() {
        // Arrange
        Project project = new Project();
        project.setName("Top Secret Project");
        em.persist(project);
        em.getTransaction().commit();

        // Act
        em.getTransaction().begin();
        em.clear(); // Clear L1 cache to force database select

        Project foundProject = em.find(Project.class, project.getId());
        Project refProject = em.getReference(Project.class, project.getId());

        // Assert
        assertNotNull(foundProject);
        assertEquals("Top Secret Project", foundProject.getName());

        assertNotNull(refProject);
        assertEquals(project.getId(), refProject.getId());
    }

    @Test
    public void testJpqlSelectAll() {
        // Arrange
        Employee e1 = new Employee();
        e1.setFirstName("Alice");
        e1.setLastName("Anderson");
        em.persist(e1);

        Employee e2 = new Employee();
        e2.setFirstName("Bob");
        e2.setLastName("Bailey");
        em.persist(e2);

        em.getTransaction().commit();

        // Act
        em.getTransaction().begin();
        em.clear();

        List<Employee> employees = em.createQuery("SELECT e FROM Employee e").getResultList();

        // Assert
        assertTrue(employees.size() >= 2);
        assertTrue(employees.stream().anyMatch(e -> "Alice".equals(e.getFirstName())));
        assertTrue(employees.stream().anyMatch(e -> "Bob".equals(e.getFirstName())));
    }

    @Test
    public void testJpqlWithParameters() {
        // Arrange
        Employee e1 = new Employee();
        e1.setFirstName("Charlie");
        e1.setLastName("Chaplin");
        em.persist(e1);

        Employee e2 = new Employee();
        e2.setFirstName("David");
        e2.setLastName("Davidson");
        em.persist(e2);

        em.getTransaction().commit();

        // Act
        em.getTransaction().begin();
        em.clear();

        Query qry = em.createQuery("SELECT e FROM Employee e WHERE e.firstName = :name");
        qry.setParameter("name", "Charlie");
        List<Employee> result = qry.getResultList();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Charlie", result.get(0).getFirstName());
    }

    @Test
    public void testJpqlJoin() {
        // Arrange
        Department dept = new Department();
        dept.setName("IT");
        em.persist(dept);

        Employee e1 = new Employee();
        e1.setFirstName("Eve");
        e1.setLastName("Evans");
        e1.setDepartment(dept);
        em.persist(e1);

        Employee e2 = new Employee();
        e2.setFirstName("Frank");
        e2.setLastName("Franklin");
        // No department
        em.persist(e2);

        em.getTransaction().commit();

        // Act
        em.getTransaction().begin();
        em.clear();

        Query qry = em.createQuery("SELECT e FROM Employee e JOIN e.department d WHERE d.name = :deptName");
        qry.setParameter("deptName", "IT");
        List<Employee> itEmployees = qry.getResultList();

        // Assert
        assertEquals(1, itEmployees.size());
        assertEquals("Eve", itEmployees.get(0).getFirstName());
    }

    @Test
    public void testAggregation() {
        // Arrange
        long initialCount = em.createQuery("SELECT COUNT(e) FROM Employee e", Long.class).getSingleResult();

        Employee e1 = new Employee();
        e1.setFirstName("Grace");
        e1.setLastName("Green");
        em.persist(e1);

        Employee e2 = new Employee();
        e2.setFirstName("Heidi");
        e2.setLastName("Harris");
        em.persist(e2);

        em.getTransaction().commit();

        // Act
        em.getTransaction().begin();
        em.clear();

        long newCount = em.createQuery("SELECT COUNT(e) FROM Employee e", Long.class).getSingleResult();

        // Assert
        assertEquals(initialCount + 2, newCount);
    }

    @Test
    public void testPaginationAndSorting() {
        // Arrange
        // Create 10 projects
        for (int i = 1; i <= 10; i++) {
            Project p = new Project();
            // Use padded numbers for string sorting: Project 01, Project 02...
            p.setName(String.format("Project %02d", i));
            em.persist(p);
        }

        em.getTransaction().commit();

        // Act
        em.getTransaction().begin();
        em.clear();

        // TODO: Write JPQL to sort by name DESC. Use setFirstResult/setMaxResults for pagination.
        Query qry = em.createQuery("SELECT p FROM Project p ORDER BY p.name DESC");
        qry.setFirstResult(0);
        qry.setMaxResults(5);

        // 1. Get Page 1 (First 5 results)
        List<Project> page1 = qry.getResultList();

        // Assert
        assertEquals(5, page1.size());
        assertEquals("Project 10", page1.get(0).getName());
        assertEquals("Project 06", page1.get(4).getName());

        // Act (Page 2)
        // 2. Get Page 2 (Next 5 results)
        qry.setFirstResult(5);
        List<Project> page2 = qry.getResultList();

        // Assert (Page 2)
        assertEquals(5, page2.size());
        assertEquals("Project 05", page2.get(0).getName());
        assertEquals("Project 01", page2.get(4).getName());
    }

    @Test
    public void testDtoProjection() {
        // Arrange
        Department dept = new Department();
        dept.setName("Finance");
        em.persist(dept);

        Employee e = new Employee();
        e.setFirstName("Ivan");
        e.setLastName("Investor");
        e.setDepartment(dept);
        em.persist(e);

        em.getTransaction().commit();

        // Act
        em.getTransaction().begin();
        em.clear();

        Query qry = em.createQuery("SELECT new com.maikelsoft.poc.jpa.dto.EmployeeDTO(e.firstName, e.lastName, d.name) FROM Employee e JOIN e.department d");
        List<EmployeeDTO> dtos = qry.getResultList();

        // Assert
        assertEquals(1, dtos.size());
        EmployeeDTO dto = dtos.get(0);
        assertEquals("Ivan", dto.getFirstName());
        assertEquals("Investor", dto.getLastName());
        assertEquals("Finance", dto.getDepartmentName());
    }

    @Test
    public void testJoinFetch() {
        // Arrange
        Project p1 = new Project();
        p1.setName("Alpha");
        em.persist(p1);

        Project p2 = new Project();
        p2.setName("Beta");
        em.persist(p2);

        Employee e = new Employee();
        e.setFirstName("Jack");
        e.setLastName("Jones");
        e.getProjects().add(p1);
        e.getProjects().add(p2);
        // Maintain bidirectional relationship if needed
        p1.getEmployees().add(e);
        p2.getEmployees().add(e);

        em.persist(e);

        em.getTransaction().commit();

        // Act
        em.getTransaction().begin();
        em.clear();

        // Fetching Employee and JOIN FETCH projects where ID matches
        TypedQuery<Employee> qry = em.createQuery("SELECT e FROM Employee e JOIN FETCH e.projects WHERE e.id = :id", Employee.class);
        qry.setParameter("id", e.getId());
        Employee fetchedEmployee = qry.getSingleResult();

        // Closing EM to detach entities (simulating end of session)
        em.close();

        // Assert
        // 3. Verify access outside transaction/session
        // If not fetched eagerly, this would throw LazyInitializationException
        assertEquals(2, fetchedEmployee.getProjects().size());
        assertTrue(fetchedEmployee.getProjects().stream().anyMatch(p -> p.getName().equals("Alpha")));
    }

    @Test
    public void testGroupBy() {
        // Arrange
        Department it = new Department();
        it.setName("IT Group");
        em.persist(it);

        Department hr = new Department();
        hr.setName("HR Group");
        em.persist(hr);

        createEmployee("Emp1", it);
        createEmployee("Emp2", it);
        createEmployee("Emp3", hr);

        em.getTransaction().commit();

        // Act
        em.getTransaction().begin();
        em.clear();

        // Writing JPQL to return Object[]: [DepartmentName, Count] ordered by DepartmentName
        Query qry = em.createQuery("SELECT d.name, COUNT(e) FROM Employee e JOIN e.department d GROUP BY d.name");
        List<Object[]> results = qry.getResultList();

        // Assert
        // Expected: HR Group -> 1, IT Group -> 2
        // Since we order by name: HR first

        assertEquals(2, results.size());

        assertEquals("HR Group", results.get(0)[0]);
        assertEquals(1L, results.get(0)[1]);

        assertEquals("IT Group", results.get(1)[0]);
        assertEquals(2L, results.get(1)[1]);
    }

    private void createEmployee(String name, Department dept) {
        Employee e = new Employee();
        e.setFirstName(name);
        e.setLastName("Test");
        e.setDepartment(dept);
        em.persist(e);
    }
}
