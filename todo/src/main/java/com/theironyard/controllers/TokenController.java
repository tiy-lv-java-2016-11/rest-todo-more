package com.theironyard.controllers;

import com.theironyard.command.LoginCommand;
import com.theironyard.entities.User;
import com.theironyard.exceptions.LoginFailedException;
import com.theironyard.exceptions.UsersNotFoundException;
import com.theironyard.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sparatan117 on 1/10/17.
 */
@RestController
public class TokenController {

    @Autowired
    UserRepository userRepository;

    public User getUserFromAuth(String token){
        String[] parts = token.split(" ");
        User savedUser = userRepository.findFirstByToken(parts[1]);

        if(savedUser == null || !savedUser.isTokenValid()){
            throw new LoginFailedException();
        }
        return savedUser;
    }

    @RequestMapping(path = "/token/", method = RequestMethod.POST)
    public Map getToken(@RequestBody LoginCommand command){
        User user = userRepository.findFirstByUsername(command.getUsername());
        if(user == null){
            throw new UsersNotFoundException();
        }

        if(!user.getPassword().equals(command.getPassword())){
            throw new LoginFailedException();
        }

        if(!user.isTokenValid()){
            user.regenerate();
        }

        Map<String , String> tokens = new HashMap<>();
        tokens.put("token", user.getToken());
        return tokens;
    }
}
