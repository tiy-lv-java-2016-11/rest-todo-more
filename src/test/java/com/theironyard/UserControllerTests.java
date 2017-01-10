package com.theironyard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theironyard.entities.User;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTests {

    @Autowired
    UserRepository userRepo;

    @Autowired
    WebApplicationContext wap;

    MockMvc mockMvc;

    @Before
    public void before(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wap).build();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        String username = "TestUsername";
        String email = "test@email.com";
        String firstName = "testFirstName";
        String lastName = "testLastName";
        User user = new User(username, email, firstName, lastName);
        userRepo.save(user);
        String username2 = "TestUsername2";
        String email2 = "test@email.com2";
        String firstName2 = "testFirstNam2e";
        String lastName2 = "testLastName2";
        User user2 = new User(username2, email2, firstName2, lastName2);
        userRepo.save(user2);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users/")
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        String content = result.getResponse().getContentAsString();
        List<User> myObjects = mapper.readValue(content, new TypeReference<List<User>>(){});


        assertEquals(myObjects.size(), 2);
        assertEquals(myObjects.get(0).getUsername(), username);
        assertEquals(myObjects.get(1).getUsername(), username2);
    }

    @Test
    public void testGetOneUser() throws Exception {
        String username = "TestUsername";
        String email = "test@email.com";
        String firstName = "testFirstName";
        String lastName = "testLastName";
        User user = new User(username, email, firstName, lastName);
        userRepo.save(user);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users/1/")
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        String content = result.getResponse().getContentAsString();
        User savedUser = mapper.readValue(content, User.class);

        assertEquals(username, savedUser.getUsername());
    }

    @Test
    public void testGetOneUserNotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/1/")
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void testCreateUser() throws Exception {
        String username = "TestUsername";
        String email = "test@email.com";
        String firstName = "testFirstName";
        String lastName = "testLastName";
        User user = new User(username, email, firstName, lastName);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/")
                        .content(json)
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        User savedUser = userRepo.findOne(1);
        assertNotNull(savedUser);
        assertEquals(savedUser.getUsername(), username);
        assertNotEquals(savedUser.getUsername(), "username");
    }

    @Test
    public void testReplaceUser() throws Exception {
        String username = "TestUsername";
        String email = "test@email.com";
        String firstName = "testFirstName";
        String lastName = "testLastName";
        User user = new User(username, email, firstName, lastName);
        userRepo.save(user);

        String username2 = "TestUsername2";
        String email2 = "test@email.com2";
        String firstName2 = "testFirstNam2e";
        String lastName2 = "testLastName2";
        User user2 = new User(username2, email2, firstName2, lastName2);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user2);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/1/")
                        .content(json)
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        User savedUser = userRepo.findOne(1);
        assertEquals(username2, savedUser.getUsername());
    }

    @Test
    public void testDeleteUser() throws Exception {
        String username = "TestUsername";
        String email = "test@email.com";
        String firstName = "testFirstName";
        String lastName = "testLastName";
        User user = new User(username, email, firstName, lastName);
        userRepo.save(user);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/1/")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        User savedUser = userRepo.findOne(1);
        assertNull(savedUser);
    }

}
