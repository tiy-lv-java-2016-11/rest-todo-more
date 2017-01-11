package com.theironyard.Controllers;

import com.theironyard.Entities.ToDoList;
import com.theironyard.Entities.User;
import com.theironyard.Repositories.ToDoListRepository;
import com.theironyard.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by darionmoore on 1/9/17.
 */
@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ToDoListRepository toDoListRepository;

    @RequestMapping(path = "/users/", method = RequestMethod.GET)
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    @RequestMapping(path = "/users/", method = RequestMethod.POST)
    public User createUser(@RequestBody User user, User token){

        userRepository.save(user);

        return user;
    }

    @RequestMapping(path = "/users/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable int id) throws Exception {
        User user = userRepository.findOne(id);
        if(user == null){
            throw new Exception();
        }
        return user;
    }

    @RequestMapping(path = "/users/{id}", method = RequestMethod.PUT)
    public User replaceUser(@PathVariable int id, @RequestBody User user){
        userRepository.save(user);
        return user;
    }

    @RequestMapping(path = "/users/{id}", method = RequestMethod.DELETE)
    public User deleteUser(@PathVariable int id){
        userRepository.delete(id);
        return null;
    }

    @RequestMapping(path = "/todos/{id}/")
    public List<ToDoList> getToDoList(@PathVariable int id) throws Exception {
        User user = userRepository.findOne(id);
        if(user == null){
            throw new Exception();
        }
        return toDoListRepository.findAllByUser(user);
    }

    @RequestMapping(path = "/todos/{id}/", method = RequestMethod.POST)
    public ToDoList createToDoList(@PathVariable int id, @RequestBody ToDoList toDoList) throws Exception {
        User user = validateUser(id);
        toDoList.setUser(user);
        toDoListRepository.save(toDoList);
        return toDoList;
    }




        public User validateUser(int id) throws Exception {
        User user = userRepository.findOne(id);
        if(user == null){
            throw new Exception();
        }
            return user;
        }

    }









