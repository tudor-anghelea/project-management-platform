package com.example.projectmanagementplatform.project;
import jakarta.persistence.*;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    protected Project() {}

    public Project(String title, String description)
    {
        this.title = title;
        this.description = description;
    }

    public Long getId() { return this.id; }

    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }
}
