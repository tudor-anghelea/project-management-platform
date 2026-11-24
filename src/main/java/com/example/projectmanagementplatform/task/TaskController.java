package com.example.projectmanagementplatform.task;
import com.example.projectmanagementplatform.project.ProjectRepository;
import com.example.projectmanagementplatform.subtask.SubtaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private SubtaskRepository subtaskRepo;
    private TaskRepository taskRepo;
    private ProjectRepository projectRepo;

    public TaskController(TaskRepository taskRepo, ProjectRepository projectRepo, SubtaskRepository subtaskRepo)
    {
        this.taskRepo = taskRepo;
        this.projectRepo = projectRepo;
        this.subtaskRepo = subtaskRepo;
    }

    @GetMapping
    public List<Task> list() { return taskRepo.findAll(); }

    @GetMapping("/{id}")
    public Task get(@PathVariable Long id)
    {
        return taskRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found."));
    }

    @GetMapping("/{project_id}/list")
    public List<Task> getFromProject(@PathVariable Long project_id)
    {
        if(!projectRepo.existsById(project_id))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found.");
        }
        return taskRepo.getAllByProjectId(project_id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestBody Task task)
    {
        if(!projectRepo.existsById(task.getProjectId()))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found.");
        }
        return taskRepo.save(task);
    }

    @PutMapping("/{id}/update-title")
    public Task updateTitle(@PathVariable Long id, @RequestBody String title)
    {
        Task task = get(id);
        task.setTitle(title);
        return taskRepo.save(task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id)
    {
        if(!taskRepo.existsById(id))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No task to delete.");
        }
        Task task = get(id);
        taskRepo.delete(task);
        subtaskRepo.deleteAllByTaskId(id);
    }
}
