package com.example.projectmanagementplatform.subtask;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Subtask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long taskId;

    @Column(nullable = false)
    private String title;

    private String description;
    private Priority priority;
    private LocalDate dueDate;
    private Integer minutes;

    private boolean completed;
    private LocalDate createdDate;

    protected Subtask() {}

    public Subtask(Long taskId, String title, String description, Priority priority, LocalDate dueDate, Integer minutes)
    {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.minutes = minutes;

        completed = false;
        this.createdDate = LocalDate.now();
    }

    public Long getId() { return this.id; }

    public Long getTaskId() { return this.taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }

    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description;}

    public Priority getPriority() { return this.priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public LocalDate getDueDate() { return this.dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Integer getMinutes() { return this.minutes; }
    public void setMinutes(Integer minutes) { this.minutes = minutes; }

    public boolean isCompleted() { return this.completed; }
    public void setCompleted() { this.completed = true; }

    public LocalDate getCreatedDate() { return this.createdDate; }
}
