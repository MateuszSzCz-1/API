package com.crud.tasks.controller;

import com.crud.tasks.domain.TaskDto;

import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskController controller;

    @Test
    void testGetEmptyTaskList() throws Exception {
        // given
        when(controller.getTasks()).thenReturn(List.of());
        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void testGetTasks() throws Exception {
        // given
        List<TaskDto> dtoList = List.of(new TaskDto(1L, "test title", "test content"));
        when(controller.getTasks()).thenReturn(dtoList);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is("test title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content", Matchers.is("test content")));
    }

    @Test
    void testGetTask() throws Exception{
        // given
        TaskDto dto = new TaskDto(2L, "test title 2", "test content 2");
        when(controller.getTask(2L)).thenReturn(dto);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/tasks/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("test title 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("test content 2")));
    }

    @Test
    void testDeleteTask() throws Exception{
        // given
        TaskDto dto = new TaskDto(3L, "test title 3", "test content 3");
        when(controller.deleteTask(3L)).thenReturn(dto);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/tasks/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void testUpdateTask() throws Exception{
        // given
        TaskDto dtoIn = new TaskDto(4L, "test title 4", "test content 4");
        TaskDto dtoOut = new TaskDto(4L, "updated task", "content after update");
        when(controller.updateTask(any(TaskDto.class))).thenReturn(dtoOut);
        Gson gson = new Gson();
        String json = gson.toJson(dtoIn);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8").content(json))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("updated task")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("content after update")));
    }

    @Test
    void testCreateTask() throws Exception{
        // given
        TaskDto dtoIn  = new TaskDto(5L, "test title 5", "test content 5");
        TaskDto dtoOut = new TaskDto(5L, "created task", "content created");
        when( controller.createTask(any(TaskDto.class))).thenReturn(dtoOut);
        Gson gson = new Gson();
        String json = gson.toJson(dtoIn);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8").content(json))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("created task")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("content created")));
    }
}
