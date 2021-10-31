package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class TaskController {

    private final DbService service;
    private final TaskMapper taskMapper;

    @RequestMapping(method = RequestMethod.GET, value = "/tasks")
    public List<TaskDto> getTasks() {
        return taskMapper.mapToTaskDtoList(service.getAllTasks());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tasks/{taskId}")
    public TaskDto getTask(@PathVariable Long taskId) throws TaskNotFoundException {
        return taskMapper.mapToTaskDto(service.getTask(taskId).orElseThrow(TaskNotFoundException::new));
    }

    // Kodilla exercise 19.3 write the method to remove one task by ID
    @RequestMapping(method = RequestMethod.DELETE, value = "/tasks/{taskId}")
    public TaskDto deleteTask(@PathVariable Long taskId) throws TaskNotFoundException {
        TaskDto tmpObject = taskMapper.mapToTaskDto(service.getTask(taskId).orElseThrow(TaskNotFoundException::new));
        service.delete(taskId);
        return tmpObject;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/tasks")
    public TaskDto updateTask(@RequestBody TaskDto taskDto) {
        return taskMapper.mapToTaskDto(service.saveTask(taskMapper.mapToTask(taskDto)));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tasks", consumes = MediaType.APPLICATION_JSON_VALUE)
    public TaskDto createTask(@RequestBody TaskDto taskDto) {
        return taskMapper.mapToTaskDto(service.saveTask(taskMapper.mapToTask(taskDto)));
    }
}