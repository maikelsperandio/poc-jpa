package com.maikelsoft.poc.jpa.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "project")
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal budget;
    private LocalDateTime createdOn;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_details_id")
    private ProjectDetails projectDetails;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<Task> tasks = new HashSet<>(0);

    @ManyToMany
    @JoinTable(name = "project_employee",
    joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"))
    private Set<Employee> employees = new HashSet<>(0);

    public Project(String name, String descr, BigDecimal budget, ProjectStatus status) {
        this.name = name;
        this.description = descr;
        this.budget = budget;
        this.status = status;
    }

    @PrePersist
    public void onPrePersist(){
        this.createdOn = LocalDateTime.now();
        System.out.println("Project being persisted: ".concat(this.name));
    }

    public void addEmployee(Employee emp) {
        this.employees.add(emp);
    }

    public void addTask(Task task){
        task.setProject(this);
        this.tasks.add(task);
    }
}
