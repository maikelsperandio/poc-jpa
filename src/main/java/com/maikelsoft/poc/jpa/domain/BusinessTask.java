package com.maikelsoft.poc.jpa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@DiscriminatorValue("business")
@NoArgsConstructor
public class BusinessTask extends Task{
    @Column(name = "meeting_url")
    private String meetingUrl;

    public BusinessTask(String title, LocalDate plusDays, String meetingUrl) {
        setTitle(title);
        setDueDate(plusDays);
        this.meetingUrl = meetingUrl;
    }
}
