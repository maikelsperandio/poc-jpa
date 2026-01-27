package com.maikelsoft.poc.jpa.identifiers;

import com.maikelsoft.poc.jpa.domain.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class EmployeeProfile {

    @Id
    private Long id;

    private String bio;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Employee employee;

    public EmployeeProfile(Employee employee, String bio) {
        this.employee = employee;
        this.bio = bio;
    }
}
