package com.theironyard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theironyard.commands.LoginCommand;
import com.theironyard.entities.User;
import com.theironyard.repositories.UserRepository;
import com.theironyard.utilities.PasswordStorage;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationControllerTests {
    User user1;
    User user2;
    String username;
    String password;

    @Autowired
    UserRepository userRepo;

    @Autowired
    WebApplicationContext wap;

    MockMvc mockMvc;

    @Before
    public void before() throws PasswordStorage.CannotPerformOperationException {
        mockMvc = MockMvcBuilders.webAppContextSetup(wap).build();

        username = "TestUsername";
        password = "testPassword";
        String email = "test@email.com";
        String firstName = "testFirstName";
        String lastName = "testLastName";
        user1 = new User(username, password, email, firstName, lastName);
        userRepo.save(user1);

        String username2 = "TestUsername2";
        String password2 = "testPassword2";
        String email2 = "test@email.com2";
        String firstName2 = "testFirstNam2e";
        String lastName2 = "testLastName2";
        user2 = new User(username2, password2, email2, firstName2, lastName2);
    }


    @Test
    public void testCreateUserValid() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user2);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register/")
                        .content(json)
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        User savedUser = userRepo.findOne(2);
        assertNotNull(savedUser);
        assertEquals(savedUser.getUsername(), user2.getUsername());
    }

    @Test
    public void testCreateUserUsernameExists() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user1);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register/")
                        .content(json)
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void testLoginValid() throws Exception {
        LoginCommand command = new LoginCommand(username, password);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(command);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/login/")
                        .content(json)
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        String tokenStr = result.getResponse().getContentAsString();
        Map<String, String > token = mapper.readValue(tokenStr, HashMap.class);

        assertEquals(user1.getToken(), token.get("token"));
    }

    @Test
    public void testLoginUserNotFound() throws Exception {
        LoginCommand command = new LoginCommand(username+"5", password);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(command);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/login/")
                        .content(json)
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testLoginPasswordInvalid() throws Exception {
        LoginCommand command = new LoginCommand(username, password+"5");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(command);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/login/")
                        .content(json)
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testLoginTokenExpired() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        user1.setExpiration(now);
        LoginCommand command = new LoginCommand(username, password);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(command);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/login/")
                        .content(json)
                        .contentType("application/json")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        String tokenStr = result.getResponse().getContentAsString();
        Map<String, String > token = mapper.readValue(tokenStr, HashMap.class);

        assertNotEquals(now, token.get("token"));
    }
}
