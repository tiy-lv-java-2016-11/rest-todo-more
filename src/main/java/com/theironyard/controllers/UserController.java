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
    public User getUser(@RequestHeader(value = "Authorization") String auth, @PathVariable int userId){
        User user = validateToken(auth);
        return user;
    }

//    @RequestMapping(path = "/", method = RequestMethod.POST)
//    public User createUser(@RequestBody User user){
//        userRepo.save(user);
//        return user;
//    }

    @RequestMapping(path = "/{userId}/", method = RequestMethod.PUT)
    public User replaceUser(@RequestHeader(value = "Authorization") String auth, @PathVariable int userId, @RequestBody User user){
        User savedUser = validateToken(auth);
        if (savedUser.getId() != userId){
            throw new UserNotAuthException();
        }
        user.setId(userId);
        userRepo.save(user);
        return user;
    }

    @RequestMapping(path = "/{userId}/", method = RequestMethod.DELETE)
    public void deleteUser(@RequestHeader(value = "Authorization") String auth, @PathVariable int userId){
        User savedUser = validateToken(auth);
        if (savedUser.getId() != userId){
            throw new UserNotAuthException();
        }
        userRepo.delete(userId);
    }

    public  User validateToken(String tokenStr){
        String[] token = tokenStr.split(" ");
        User user = userRepo.findByToken(token[1]);
        if (user == null){
            throw new UserNotFoundException();
        }
        else if (!user.isTokenValid()){
            throw new TokenExpiredException();
        }
        return user;
    }
}
