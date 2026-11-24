package com.example.projectmanagementplatform.analytics;
import com.example.projectmanagementplatform.file.ServiceExport;
import com.example.projectmanagementplatform.file.ServiceImport;
import com.example.projectmanagementplatform.project.Project;
import com.example.projectmanagementplatform.project.ProjectRepository;
import com.example.projectmanagementplatform.subtask.Subtask;
import com.example.projectmanagementplatform.subtask.SubtaskRepository;
import com.example.projectmanagementplatform.task.Task;
import com.example.projectmanagementplatform.task.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/projects")
public class AnalyticsController {

    private final ObjectMapper objectMapper;

    private ProjectRepository projectRepo;
    private TaskRepository taskRepo;
    private SubtaskRepository subtaskRepo;

    public AnalyticsController(ProjectRepository projectRepo, TaskRepository taskRepo, SubtaskRepository subtaskRepo, ObjectMapper objectMapper)
    {
        this.projectRepo = projectRepo;
        this.taskRepo = taskRepo;
        this.subtaskRepo = subtaskRepo;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/{id}/analytics/completion-rate")
    public String completionRate(@PathVariable Long id)
    {
        if(!projectRepo.existsById(id))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found.");
        }
        List<Task> tasks = taskRepo.findAllByProjectId(id);
        List<Subtask> subtasks = new ArrayList<>();
        for(Task t: tasks) subtasks.addAll(subtaskRepo.findAllByTaskId(t.getId()));

        int completed = Math.toIntExact(subtasks.stream().filter(Subtask::isCompleted).count());
        int all = subtasks.size();

        float rate = ((float) completed / all) * 100;
        return rate + "%";
    }

    @GetMapping("/{id}/analytics/overdue-rate")
    public String overdueRate(@PathVariable Long id)
    {
        if(!projectRepo.existsById(id))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found.");
        }
        List<Task> tasks = taskRepo.findAllByProjectId(id);
        List<Subtask> subtasks = new ArrayList<>();
        for(Task t: tasks) subtasks.addAll(subtaskRepo.findAllByTaskId(t.getId()));

        int completed = Math.toIntExact(subtasks.stream()
                .filter(s -> !s.isCompleted() && s.getDueDate().isBefore(LocalDate.now())).count());
        int all = Math.toIntExact(subtasks.stream().filter(s -> !s.isCompleted()).count());

        float rate = ((float) completed / all) * 100;
        return rate + "%";
    }

    @GetMapping("/{id}/analytics/overview")
    public Overview overview(@PathVariable Long id) {
        Project project = projectRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found."));

        List<Task> tasks = taskRepo.findAllByProjectId(id);
        List<Subtask> subtasks = new ArrayList<>();
        for (Task t : tasks) subtasks.addAll(subtaskRepo.findAllByTaskId(t.getId()));

        List<TaskOverview> taskOverviews = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i++)
        {
            int finalI = i;
            List<Subtask> s = subtasks.stream().filter(sub -> Objects.equals(sub.getTaskId(), tasks.get(finalI).getId())).toList();
            taskOverviews.add(new TaskOverview(tasks.get(finalI), s));
        }
        return new Overview(project, taskOverviews);
    }

    @GetMapping("/{id}/export")
    public ResponseEntity<byte[]> export(@PathVariable Long id) throws JsonProcessingException
    {
        Overview overview = overview(id);

        byte[] json = new ServiceExport(objectMapper).exportProject(overview);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=overview.json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Overview> importOverview(@RequestParam("file") MultipartFile file) throws IOException {

        if (file.isEmpty()) return ResponseEntity.badRequest().build();
        Overview overview = new ServiceImport(objectMapper).importOverview(file.getBytes());

        return ResponseEntity.ok(overview);
    }
}
