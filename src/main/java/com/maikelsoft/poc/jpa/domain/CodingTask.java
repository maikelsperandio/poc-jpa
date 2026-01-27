package com.maikelsoft.poc.jpa.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@DiscriminatorValue("coding")
@NoArgsConstructor
public class CodingTask extends Task{
    private String language;

    public CodingTask(String feature, LocalDate plusDays, String language) {
        setTitle(feature);
        setDueDate(plusDays);
        this.language = language;
    }
}
