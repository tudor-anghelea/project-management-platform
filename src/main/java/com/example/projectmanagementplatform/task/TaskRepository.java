package com.example.projectmanagementplatform.task;

import com.example.projectmanagementplatform.subtask.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> getAllByProjectId(Long projectId);

    void deleteAllByProjectId(Long projectId);

    List<Task> findAllByProjectId(Long projectId);
}
