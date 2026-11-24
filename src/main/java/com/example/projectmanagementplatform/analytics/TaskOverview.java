package com.example.projectmanagementplatform.analytics;
import com.example.projectmanagementplatform.subtask.Subtask;
import com.example.projectmanagementplatform.task.Task;
import java.util.List;

public record TaskOverview(Task task, List<Subtask> subtasks) {
}
