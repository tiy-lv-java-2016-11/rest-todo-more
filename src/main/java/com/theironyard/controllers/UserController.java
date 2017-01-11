package com.theironyard.controllers;

import com.theironyard.entities.User;
import com.theironyard.exceptions.TokenExpiredException;
import com.theironyard.exceptions.UserNotAuthException;
import com.theironyard.exceptions.UserNotFoundException;
import com.theironyard.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    UserRepository userRepo;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public List<User> getUsers(){
        return userRepo.findAll();
    }

    @RequestMapping(path = "/{userId}/", method = RequestMethod.GET)
    public User getUser(@PathVariable int userId){
        User user = userRepo.findOne(userId);
        if (user == null){
            throw new UserNotFoundException();
        }
        return user;
    }

    @RequestMapping(path = "/{userId}/", method = RequestMethod.PUT)
    public User replaceUser(@RequestHeader(value = "Authorization") String auth, @PathVariable int userId, @RequestBody User user){
        validateUser(auth, userId);
        user.setId(userId);
        userRepo.save(user);
        return user;
    }

    @RequestMapping(path = "/{userId}/", method = RequestMethod.DELETE)
    public void deleteUser(@RequestHeader(value = "Authorization") String auth, @PathVariable int userId){
        User savedUser = validateUser(auth, userId);
        if (savedUser.getId() != userId){
            throw new UserNotAuthException();
        }
        userRepo.delete(userId);
    }

    public User validateUser(String tokenStr, int userId){
        String token = tokenStr.split(" ")[1];
        User user = userRepo.findByToken(token);

        if (user == null || user.getId() != userId){
            throw new UserNotAuthException();
        }
        else if (!user.isTokenValid()){
            throw new TokenExpiredException();
        }
        return user;
    }
}
