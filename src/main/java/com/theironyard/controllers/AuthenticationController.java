package com.theironyard.controllers;

import com.theironyard.commands.LoginCommand;
import com.theironyard.entities.User;
import com.theironyard.exceptions.LoginFailedException;
import com.theironyard.exceptions.UserNotFoundException;
import com.theironyard.exceptions.UsernameExistsException;
import com.theironyard.repositories.TodoRepository;
import com.theironyard.repositories.UserRepository;
import com.theironyard.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthenticationController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    TodoRepository todoRepo;

    @RequestMapping(path = "/login/", method = RequestMethod.POST)
    public Map login(@RequestBody LoginCommand command) throws PasswordStorage.InvalidHashException, PasswordStorage.CannotPerformOperationException {
        Map<String, String> token = new HashMap<>();
        User user = userRepo.findByUsername(command.getUsername());
        if (user == null) {
            throw new UserNotFoundException();
        }
        else if (PasswordStorage.verifyPassword(command.getPassword(), user.getPassword())){
            if (!user.isTokenValid()){
                user.regenerateToken();
            }
        }
        else {
            throw new LoginFailedException();
        }
        token.put("token", user.getToken());
        return token;
    }

    @RequestMapping(path = "/register/", method = RequestMethod.POST)
    public User createUser(@RequestBody User user){
        User savedUser = userRepo.findByUsername(user.getUsername());
        if (savedUser != null){
            throw new UsernameExistsException();
        }
        userRepo.save(user);
        return user;
    }
}
