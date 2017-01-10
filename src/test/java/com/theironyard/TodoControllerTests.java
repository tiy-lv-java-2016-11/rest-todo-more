package com.theironyard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theironyard.entities.Todo;
import com.theironyard.entities.User;
import com.theironyard.repositories.TodoRepository;
import com.theironyard.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TodoControllerTests {
    private String username = "TestUsername";
    private String email = "test@email.com";
    private String firstName = "testFirstName";
    private String lastName = "testLastName";
    User user;

    @Autowired
    TodoRepository todoRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    WebApplicationContext wap;

    MockMvc mockMvc;

    @Before
    public void before(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wap).build();
        user = new User(username, email, firstName, lastName);
        userRepo.save(user);
    }

    @Test
    public void testGetAllTodosForUser() throws Exception {
        String title = "title";
        String description = "description";
        String dueDate = "2017-12-12";
        Todo todo = new Todo(title, description, dueDate, user);
        todoRepo.save(todo);
        String title2 = "title2";
        String description2 = "description2";
        String dueDate2 = "2018-12-12";
        Todo todo2 = new Todo(title2, description2, dueDate2, user);
        todoRepo.save(todo2);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users/1/todos/")
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        String content = result.getResponse().getContentAsString();
        List<Todo> myObjects = mapper.readValue(content, new TypeReference<List<Todo>>(){});


        assertEquals(myObjects.size(), 2);
        assertEquals(myObjects.get(0).getTitle(), title);
        assertEquals(myObjects.get(1).getTitle(), title2);
    }

    @Test
    public void testGetOneTodoForUser() throws Exception {
        String title = "title";
        String description = "description";
        String dueDate = "2017-12-12";
        Todo todo = new Todo(title, description, dueDate, user);
        todoRepo.save(todo);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users/1/todos/1/")
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        String content = result.getResponse().getContentAsString();
        Todo savedTodo = mapper.readValue(content, Todo.class);

        assertEquals(savedTodo.getTitle(), title);
    }

    @Test
    public void testGetOneTodoNotFoundForUser() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/1/todos/1/")
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void testCreateTodoForUser() throws Exception {
        String title = "title";
        String description = "description";
        String dueDate = "2017-12-12";
        Todo todo = new Todo(title, description, dueDate, user);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(todo);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/1/todos/")
                        .content(json)
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        Todo savedTodo = todoRepo.findOne(1);
        assertNotNull(savedTodo);
        assertEquals(title, savedTodo.getTitle());
    }

    @Test
    public void testReplaceTodoForUser() throws Exception {
        String title = "title";
        String description = "description";
        String dueDate = "2017-12-12";
        Todo todo = new Todo(title, description, dueDate, user);
        todoRepo.save(todo);
        String title2 = "title2";
        String description2 = "description2";
        String dueDate2 = "2018-12-12";
        Todo todo2 = new Todo(title2, description2, dueDate2, user);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(todo2);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/1/todos/1/")
                        .content(json)
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        Todo savedTodo = todoRepo.findOne(1);
        assertNotNull(savedTodo);
        assertEquals(title2, savedTodo.getTitle());
    }

    @Test
    public void testDeleteTodoForUser() throws Exception {
        String title = "title";
        String description = "description";
        String dueDate = "2017-12-12";
        Todo todo = new Todo(title, description, dueDate, user);
        todoRepo.save(todo);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/1/todos/1/")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        Todo savedTodo = todoRepo.findOne(1);
        assertNull(savedTodo);

    }
}
