package com.theironyard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theironyard.controllers.UserController;
import com.theironyard.entities.User;
import com.theironyard.exceptions.UserNotAuthException;
import com.theironyard.repositories.UserRepository;
import com.theironyard.utilities.PasswordStorage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTests {
    User user1;
    User user2Replace;
    User user2Delete;

    @Autowired
    UserRepository userRepo;

    @Autowired
    WebApplicationContext wap;

    @Autowired
    UserController userController;

    MockMvc mockMvc;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void before() throws PasswordStorage.CannotPerformOperationException {
        mockMvc = MockMvcBuilders.webAppContextSetup(wap).build();

        String username = "TestUsername";
        String email = "test@email.com";
        String password = "testPassword";
        String firstName = "testFirstName";
        String lastName = "testLastName";
        user1 = new User(username, password, email, firstName, lastName);
        userRepo.save(user1);

        String username2 = "TestUsername2";
        String password2 = "testPassword2";
        String email2 = "test@email.com2";
        String firstName2 = "testFirstNam2e";
        String lastName2 = "testLastName2";
        user2Replace = new User(username2, password2, email2, firstName2, lastName2);
        userRepo.save(user2Replace);

        String username3 = "TestUsername5";
        String password3 = "testPassword5";
        String email3 = "test@email.com5";
        String firstName3 = "testFirstNam5e";
        String lastName3 = "testLastName5";
        user2Delete = new User(username3, password3, email3, firstName3, lastName3);
        userRepo.save(user2Delete);

    }

    @Test
    public void testGetAllUsers() throws Exception {

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users/")
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        String content = result.getResponse().getContentAsString();
        List<User> myObjects = mapper.readValue(content, new TypeReference<List<User>>(){});

        assertEquals(myObjects.size(), 2);
        assertEquals(myObjects.get(0).getUsername(), user1.getUsername());
        assertEquals(myObjects.get(1).getUsername(), user2Replace.getUsername());
    }

    @Test
    public void testGetOneUser() throws Exception {

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users/1/")
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        String content = result.getResponse().getContentAsString();
        User savedUser = mapper.readValue(content, User.class);

        assertEquals(user1.getUsername(), savedUser.getUsername());
    }

    @Test
    public void testGetOneUserNotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/10/")
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    public void testReplaceUser() throws Exception {

        String username = "TestUsername3";
        String password = "testPassword3";
        String email = "test@email.com3";
        String firstName = "testFirstNam3e";
        String lastName = "testLastName3";
        User replaceUser = new User(username, password, email, firstName, lastName);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(replaceUser);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/2/")
                        .header("Authorization", "Token " + user2Replace.getToken())
                        .content(json)
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        User savedUser = userRepo.findOne(2);
        assertEquals(username, savedUser.getUsername());
    }

    @Test
    public void testDeleteUser() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/3/")
                        .header("Authorization", "Token " + user2Delete.getToken())
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        User savedUser = userRepo.findOne(3);
        assertNull(savedUser);
    }

    @Test
    public void testValidateUser(){
        String token = "Token " + user1.getToken();
        User user = userController.validateUser(token, user1.getId());

        assertEquals(user.getToken(), user1.getToken());
    }

    @Test
    public void testValidateUserNotFound(){
        String token = "Token " + user2Replace.getToken();

        thrown.expect(UserNotAuthException.class);
        userController.validateUser(token, user1.getId());
    }

    @Test
    public void testValidateUserNotAuthorized(){
        String token = "Token " + user1.getToken();

        thrown.expect(UserNotAuthException.class);
        userController.validateUser(token, user2Replace.getId());

    }

    @Test
    public void testValidateUserTokenExpired(){
        String token = "Token " + user1.getToken();
        user1.setExpiration(LocalDateTime.now());
        userRepo.save(user1);
        User user = userController.validateUser(token, user1.getId());
        assertNull(user);
    }
}
