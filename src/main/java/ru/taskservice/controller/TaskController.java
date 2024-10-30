package ru.taskservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.taskservice.dto.CreateTaskRequest;
import ru.taskservice.dto.UpdateTaskFieldRequest;
import ru.taskservice.dto.UpdateTimeRequest;
import ru.taskservice.model.*;
import ru.taskservice.service.TaskService;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskManager;

    @PostMapping("/create")
    public String addTask(@RequestBody CreateTaskRequest request) {
        Task task = new Task(
                request.getName(),
                request.getDescription(),
                request.getDefaultStatus(),
                request.getTimeToComplete());

        boolean success = taskManager.addTask(task);
        System.out.println(task);
        return success ? "Task created" : "Task not created";
    }

    @PostMapping("/{id}/update/time")
    public String updateTimeToComplete(@PathVariable("id") UUID id, @RequestBody UpdateTimeRequest request) {
        boolean success = taskManager.updateTimeToComplete(id, request);
        return success ? "Time updated successfully" : "Time not updated";
    }

    @PostMapping("/{id}/update/name")
    public String updateName(@PathVariable("id") UUID id, @RequestBody UpdateTaskFieldRequest request) {
        boolean success = taskManager.updateTaskField(id, request);
        return success ? "Name update successfully" : "Name not updated";
    }

    @PostMapping("/{id}/update/description")
    public String updateDescription(@PathVariable("id") UUID id, @RequestBody UpdateTaskFieldRequest request) {
        boolean success = taskManager.updateTaskField(id, request);
        return success ? "Description update successfully" : "Description not updated";
    }

    @PostMapping("/{id}/update/status")
    public String updateStatus(@PathVariable("id") UUID id, @RequestBody UpdateTaskFieldRequest request) {
        boolean success = taskManager.updateTaskField(id, request);
        return success ? "Status update successfully" : "Status not updated";
    }

    @DeleteMapping("/{id}/delete")
    public String removeTask(@PathVariable("id") UUID id) {
        boolean success = taskManager.removeTask(id);
        return success ? "Task removed successfully" : "Task not found";
    }

    @GetMapping
    public Collection<Task> getAllTasks() {
        return taskManager.getAllTasks();
    }

    @GetMapping("/search")
    public Collection<Task> findTasks(@RequestParam("keyword") String keyword) {
        return taskManager.findTasks(keyword);
    }
}
