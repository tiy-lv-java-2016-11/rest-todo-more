package com.theironyard;


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
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TodoApplicationTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
	TodoRepository todoRepository;

	@Autowired
	WebApplicationContext wap;

    MockMvc mockMvc;

	@Before
	public void before(){
		mockMvc = MockMvcBuilders.webAppContextSetup(wap).build();

	}
	@Test
	public void contextLoads() {
	}

	@Test
	public void testCreateUser() throws Exception {
		User user = new User("testuser", "fake@random.com", "no", "name");

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(user);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/users/")
				.content(json)
				.contentType("application/json")
		).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
		User user1 = userRepository.findOne(1);
		assertNotNull(user1);
	}

	@Test
    public void testCreateTodo() throws Exception {
	    User user = new User("random", "fake@email.com", "john", "smith");
	    userRepository.save(user);
        Todo todo = new Todo("test", "testing create", "due now", user);
        todoRepository.save(todo);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(todo);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/1/todos/")
                        .content(json)
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        Todo todo1 = todoRepository.findOne(1);
        assertNotNull(todo1);
    }

}
