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
public class EmployeeSkill {

    @EmbeddedId
    private EmployeeSkillId id;

    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private Skill skill;

    private int proficiencyLevel;

    public EmployeeSkill(Employee employee, Skill skill, int proficiencyLevel) {
        this.employee = employee;
        this.skill = skill;
        this.proficiencyLevel = proficiencyLevel;
        this.id =  new EmployeeSkillId(employee.getId(), skill.getId());
    }

}
