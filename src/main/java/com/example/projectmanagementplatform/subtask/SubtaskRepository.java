package com.example.projectmanagementplatform.subtask;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
    List<Subtask> getAllByTaskId(Long task_id);

    void deleteAllByTaskId(Long id);

    boolean existsByTaskId(Long taskId);

    List<Subtask> findAllByTaskId(Long taskId);
}
