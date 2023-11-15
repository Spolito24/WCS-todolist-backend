package com.wildcodeschool.todolistback.controller;

import com.wildcodeschool.todolistback.model.Task;
import com.wildcodeschool.todolistback.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return new ResponseEntity<>(taskRepository.save(task), HttpStatus.CREATED);
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable long id, @RequestBody Task task) {
        Optional<Task> taskToUpdate = this.taskRepository.findById(id);
        if (taskToUpdate.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Task taskUpdated = taskToUpdate.get();
            taskUpdated.setTitle(task.getTitle());
            taskUpdated.setDescription(task.getDescription());
            taskUpdated.setDone(task.isDone());
            return new ResponseEntity<>(taskRepository.save(taskUpdated), HttpStatus.OK);
        }
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable long id) {
        this.taskRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

