package com.theironyard.controllers;

import com.theironyard.entities.Todo;
import com.theironyard.entities.User;
import com.theironyard.exceptions.NotOwnerException;
import com.theironyard.exceptions.TodoNotFoundException;
import com.theironyard.repositories.TodoRepository;
import com.theironyard.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by sparatan117 on 1/10/17.
 */
@RestController
@RequestMapping(path = "/todos/")
public class TodoController {
    @Autowired
    TodoRepository todoRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenController tokenController;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public List<Todo> getTodos(){
        return todoRepository.findAll();
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public Todo createTodo(@RequestHeader(value = "Authorization") String auth, HttpServletResponse response,
                           @RequestBody Todo todo){
        User savedUser = tokenController.getUserFromAuth(auth);
        todo.setUser(savedUser);
        todoRepository.save(todo);

        response.setStatus(HttpServletResponse.SC_CREATED);
        return todo;
    }

    @RequestMapping(path = "/{todoId}/", method = RequestMethod.GET)
    public Todo getTodo(@PathVariable int todoId){
        Todo todo = todoRepository.findOne(todoId);

        if(todo == null){
            throw new TodoNotFoundException();
        }
        return todo;
    }

    @RequestMapping(path = "/{todoId}/", method = RequestMethod.PUT)
    public Todo updateTodo(@RequestHeader(value = "Authorization") String auth, HttpServletResponse response,
                           @RequestBody Todo todo, @PathVariable int todoId){
        User savedUser = tokenController.getUserFromAuth(auth);
        Todo savedTodo = todoRepository.findOne(todoId);

        if(savedUser.getId() != savedTodo.getUser().getId()){
            throw new NotOwnerException();
        }

        todo.setId(savedTodo.getId());
        todoRepository.save(todo);

        response.setStatus(HttpServletResponse.SC_CREATED);
        return todo;
    }
}
