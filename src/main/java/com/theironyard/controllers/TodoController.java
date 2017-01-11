package com.theironyard.controllers;

import com.theironyard.entities.Todo;
import com.theironyard.entities.User;
import com.theironyard.exceptions.TodoNotFoundException;
import com.theironyard.exceptions.UserNotAuthException;
import com.theironyard.exceptions.UserNotFoundException;
import com.theironyard.repositories.TodoRepository;
import com.theironyard.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/todos")
public class TodoController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    TodoRepository todoRepo;

    @Autowired
    UserController userController;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public List<Todo> getTodos(@RequestHeader(value = "Authorization") String auth){
        User user = userController.validateUser(auth);
        return todoRepo.findByUser(user);
    }

    @RequestMapping(path = "/{todoId}/", method = RequestMethod.GET)
    public Todo getTodo(@RequestHeader(value = "Authorization") String auth, @PathVariable int todoId){
        User user = userController.validateUser(auth);
        Todo todo = validateTodo(todoId);

        return validateTodoUser(todo, user);
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public Todo createTodo(@RequestHeader(value = "Authorization") String auth, @RequestBody Todo todo){
        User user = userController.validateUser(auth);
        todo.setUser(user);
        todoRepo.save(todo);
        return todo;
    }

    @RequestMapping(path = "/{todoId}/", method = RequestMethod.PUT)
    public Todo replaceTodo(@RequestHeader(value = "Authorization") String auth, @PathVariable int todoId, @RequestBody Todo todo){
        User user = userController.validateUser(auth);
        Todo savedTodo = validateTodo(todoId);
        validateTodoUser(savedTodo, user);
        todo.setId(todoId);
        todo.setUser(user);
        todoRepo.save(todo);
        return todo;
    }

    @RequestMapping(path = "/{todoId}/", method = RequestMethod.DELETE)
    public void deleteTodo(@RequestHeader(value = "Authorization") String auth, @PathVariable int todoId){
        User user = userController.validateUser(auth);
        Todo savedTodo = validateTodo(todoId);
        validateTodoUser(savedTodo, user);
        todoRepo.delete(todoId);
    }

    public Todo validateTodoUser(Todo todo, User user){
        if (todo.getUser() != user){
            throw new UserNotAuthException();
        }
        return todo;
    }

    public Todo validateTodo(int todoId){
        Todo todo = todoRepo.findOne(todoId);
        if (todo == null){
            throw new TodoNotFoundException();
        }
        return todo;
    }

    public User validateUser(int userId){
        User user = userRepo.findOne(userId);
        if (user == null){
            throw new UserNotFoundException();
        }
        return user;
    }

}
