package com.theironyard.controllers;

import com.theironyard.Utillities.PasswordStorage;
import com.theironyard.entities.Todo;
import com.theironyard.entities.User;
import com.theironyard.exceptions.LoginFailedException;
import com.theironyard.exceptions.UsersNotFoundException;
import com.theironyard.repositories.TodoRepository;
import com.theironyard.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by sparatan117 on 1/9/17.
 */
@RestController
@RequestMapping(path = "/users/")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    TokenController tokenController;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public User createUser(@RequestBody User user) throws PasswordStorage.CannotPerformOperationException, PasswordStorage.InvalidHashException {
        User user1 = userRepository.findFirstByUsername(user.getUsername());
        String pass = user.getPassword();
        if(user1 == null) {
            String newPass = PasswordStorage.createHash(pass);
            user.setPassword(newPass);
            userRepository.save(user);
        }
        else if(!PasswordStorage.verifyPassword(pass, user1.getPassword())){
            throw new LoginFailedException();
        }
        return user;
    }

    @RequestMapping(path = "/{userId}/", method = RequestMethod.GET)
    public User getUser(@PathVariable int userId){
        User user = validateUser(userId);
        return user;
    }

    @RequestMapping(path = "/{userId}/", method = RequestMethod.PUT)
    public User replaceUser(@RequestHeader(value = "Authorization") String auth,
                            HttpServletResponse response,@PathVariable int userId, @RequestBody User user){

        User savedUser = tokenController.getUserFromAuth(auth);
        user.setId(userId);
        userRepository.save(savedUser);

        response.setStatus(HttpServletResponse.SC_CREATED);
        return user;
    }

    @RequestMapping(path = "/{userId}/", method = RequestMethod.DELETE)
    public void deleteUser(@RequestHeader(value = "Authorization")String auth, @PathVariable int userId){
        User savedUser = tokenController.getUserFromAuth(auth);
        if(savedUser != null){
            userRepository.delete(userId);
        }
    }

    @RequestMapping(path = "/{userId}/todos/", method = RequestMethod.GET)
    public List<Todo> getTodo(@PathVariable int userId){
        User user = validateUser(userId);
        return todoRepository.findByUser(user);
    }


    public User validateUser(int userId){
        User user = userRepository.findOne(userId);
        if(user == null){
            throw new UsersNotFoundException();
        }
        return user;
    }

}
