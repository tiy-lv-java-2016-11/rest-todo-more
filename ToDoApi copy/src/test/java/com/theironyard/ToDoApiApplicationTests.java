package com.theironyard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theironyard.Entities.ToDoList;
import com.theironyard.Entities.User;
import com.theironyard.Repositories.ToDoListRepository;
import com.theironyard.Repositories.UserRepository;
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
@WebAppConfiguration
public class ToDoApiApplicationTests {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ToDoListRepository toDoListRepository;

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
		User user = new User();

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(user);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/users/")
				.content(json)
				.contentType("application/json")
		).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

		User savedUser = userRepository.findOne(1);
		assertNotNull(savedUser);
		assertEquals(user, user);
	}

	@Test
	public void testCreateToDo() throws Exception {
		ToDoList toDoList = new ToDoList();

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(toDoList);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/todos/")
				.content(json)
				.contentType("application/json")
		).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

		ToDoList savedToDoList = toDoListRepository.findOne(1);
		assertNotNull(savedToDoList);
		assertEquals(toDoList, toDoList);
	}

}
