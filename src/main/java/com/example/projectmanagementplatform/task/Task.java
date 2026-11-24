package com.example.projectmanagementplatform.task;
import jakarta.persistence.*;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId;

    @Column(nullable = false)
    private String title;

    protected Task() {}

    public Task(Long projectId, String title)
    {
        this.projectId = projectId;
        this.title = title;
    }

    public Long getId() { return this.id; }
    public Long getProjectId() { return this.projectId; }

    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }
}
