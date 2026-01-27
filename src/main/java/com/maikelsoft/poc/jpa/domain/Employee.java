package com.maikelsoft.poc.jpa.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@SecondaryTable(name = "employee_salary", pkJoinColumns = @PrimaryKeyJoinColumn(name = "employee_id"))
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(table = "employee_salary")
    private BigDecimal salary;

    @Embedded
    private Address address;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToMany(mappedBy = "employees")
    private Set<Project> projects = new HashSet<>(0);

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "access_card_id")
    private AccessCard accessCard;

    public Employee(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
