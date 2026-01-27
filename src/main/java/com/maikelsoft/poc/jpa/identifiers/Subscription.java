package com.maikelsoft.poc.jpa.identifiers;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sub_seq_gen")
    @SequenceGenerator(
            name = "sub_seq_gen",
            sequenceName = "subscription_sequence",
            allocationSize = 1)
    private Long id;

    private String name;
    private double value;

    public Subscription(String name, double value) {
        this.name = name;
        this.value = value;
    }
}
