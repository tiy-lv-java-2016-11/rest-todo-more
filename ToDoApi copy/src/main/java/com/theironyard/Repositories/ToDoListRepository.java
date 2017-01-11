package com.theironyard.Repositories;

import com.theironyard.Entities.ToDoList;
import com.theironyard.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by darionmoore on 1/9/17.
 */
public interface ToDoListRepository extends JpaRepository<ToDoList, Integer> {
    List<ToDoList> findAllByUser(User user);

}
