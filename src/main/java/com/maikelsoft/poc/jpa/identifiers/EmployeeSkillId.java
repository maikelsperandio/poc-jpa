package com.maikelsoft.poc.jpa.identifiers;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class EmployeeSkillId {

    private Long employeeId;
    private Long skillId;

    public EmployeeSkillId(Long employeeId, Long skillId) {
        this.employeeId = employeeId;
        this.skillId = skillId;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeSkillId that = (EmployeeSkillId) o;
        return Objects.equals(employeeId, that.employeeId) && Objects.equals(skillId, that.skillId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, skillId);
    }
}
