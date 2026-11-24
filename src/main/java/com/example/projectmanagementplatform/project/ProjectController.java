package com.example.projectmanagementplatform.project;
import com.example.projectmanagementplatform.task.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private ProjectRepository repo;
    private TaskRepository taskRepo;

    public ProjectController(ProjectRepository repo, TaskRepository taskRepo)
    {
        this.repo = repo;
        this.taskRepo = taskRepo;
    }

    @GetMapping
    public List<Project> list() { return repo.findAll(); }

    @GetMapping("/{id}")
    public Project get(@PathVariable Long id)
    {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found."));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Project create(@RequestBody Project project)
    {
        return repo.save(project);
    }

    @PutMapping("/{id}/update-title")
    public Project updateTitle(@PathVariable Long id, @RequestBody String title)
    {
        Project project = get(id);
        project.setTitle(title);
        return repo.save(project);
    }

    @PutMapping("/{id}/update-description")
    public Project updateDescription(@PathVariable Long id, @RequestBody String description)
    {
        Project project = get(id);
        project.setDescription(description);
        return repo.save(project);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id)
    {
        if(!repo.existsById(id))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No task to delete.");
        }
        Project project = get(id);
        repo.delete(project);
        taskRepo.deleteAllByProjectId(id);
    }
}
