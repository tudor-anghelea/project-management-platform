package com.example.projectmanagementplatform.analytics;
import com.example.projectmanagementplatform.project.Project;
import com.example.projectmanagementplatform.task.Task;
import java.util.List;

public record Overview(Project project, List<TaskOverview> taskOverviews) {
}
