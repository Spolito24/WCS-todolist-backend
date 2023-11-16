package com.wildcodeschool.todolistback.controller;

import com.wildcodeschool.todolistback.model.Task;
import com.wildcodeschool.todolistback.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class TaskController {
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/task")
    public ResponseEntity<List<Task>> getAllTasks() {
        return new ResponseEntity<>(taskRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/task")
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        if (task == null) {
            return ResponseEntity.badRequest().body("Task data must not be null");
        }
        try {
            return ResponseEntity.ok(taskRepository.save(task));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating task");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task task) {
        if (task == null) {
            return ResponseEntity.badRequest().body("Task data must not be null");
        }

        try {
            Optional<Task> taskToUpdate = taskRepository.findById(id);
            if (taskToUpdate.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task Not Found");
            }
            Task taskUpdated = taskToUpdate.get();
            taskUpdated.setTitle(task.getTitle());
            taskUpdated.setDescription(task.getDescription());
            taskUpdated.setDone(task.isDone());

            return ResponseEntity.ok(taskRepository.save(taskUpdated));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating task");
        }
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable long id) {
        try {
            taskRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Task deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting task");
        }
    }
}

