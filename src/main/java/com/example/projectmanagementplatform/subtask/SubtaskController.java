package com.example.projectmanagementplatform.subtask;
import com.example.projectmanagementplatform.task.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/subtasks")
public class SubtaskController {

    private SubtaskRepository subtaskRepo;
    private TaskRepository taskRepo;

    public SubtaskController(SubtaskRepository subtaskRepo, TaskRepository taskRepo)
    {
        this.subtaskRepo = subtaskRepo;
        this.taskRepo = taskRepo;
    }

    @GetMapping
    public List<Subtask> list(){ return subtaskRepo.findAll(); }

    @GetMapping("/{id}")
    public Subtask get(@PathVariable Long id)
    {
        return subtaskRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subtask not found."));
    }

    @GetMapping("/{task_id}/list")
    public List<Subtask> getFromTask(@PathVariable Long task_id)
    {
        if(!taskRepo.existsById(task_id))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found.");
        }
        return subtaskRepo.getAllByTaskId(task_id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Subtask create(@RequestBody Subtask subtask)
    {
        if(!taskRepo.existsById(subtask.getTaskId()))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found.");
        }
        return subtaskRepo.save(subtask);
    }

    @PutMapping("/{id}/update-task")
    public Subtask updateTask(@PathVariable Long id, @RequestBody Long task_id)
    {
        Subtask subtask = get(id);
        if(!taskRepo.existsById(subtask.getTaskId()))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found.");
        }
        subtask.setTaskId(task_id);
        return subtaskRepo.save(subtask);
    }

    @PutMapping("/{id}/update-title")
    public Subtask updateTitle(@PathVariable Long id, @RequestBody String title)
    {
        Subtask subtask = get(id);
        subtask.setTitle(title);
        return subtaskRepo.save(subtask);
    }

    @PutMapping("/{id}/update-description")
    public Subtask updateDescription(@PathVariable Long id, @RequestBody String description)
    {
        Subtask subtask = get(id);
        subtask.setDescription(description);
        return subtaskRepo.save(subtask);
    }

    @PutMapping("/{id}/update-priority")
    public Subtask updatePriority(@PathVariable Long id, @RequestBody Priority priority)
    {
        Subtask subtask = get(id);
        subtask.setPriority(priority);
        return subtaskRepo.save(subtask);
    }

    @PutMapping("/{id}/update-duedate")
    public Subtask updateDueDate(@PathVariable Long id, @RequestBody LocalDate dueDate)
    {
        Subtask subtask = get(id);
        if(dueDate.isBefore(LocalDate.now()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Due date before current time.");
        }
        subtask.setDueDate(dueDate);
        return subtaskRepo.save(subtask);
    }

    @PutMapping("/{id}/completed")
    public Subtask completed(@PathVariable Long id)
    {
        Subtask subtask = get(id);
        if(subtask.isCompleted())
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Subtask already completed.");
        }
        subtask.setCompleted();
        return subtaskRepo.save(subtask);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id)
    {
        if(!subtaskRepo.existsById(id))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No subtask to delete.");
        }
        Subtask subtask = get(id);
        subtaskRepo.delete(subtask);
    }
}
