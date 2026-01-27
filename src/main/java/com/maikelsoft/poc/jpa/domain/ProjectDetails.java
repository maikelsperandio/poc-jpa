package com.maikelsoft.poc.jpa.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "project_detail")
public class ProjectDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "repository_url")
    private String repositoryUrl;

    private String manager;

    @OneToOne(mappedBy = "projectDetails")
    private Project project;

    public ProjectDetails(String repoUrl, String manager) {
        this.repositoryUrl = repoUrl;
        this.manager = manager;
    }
}
