package com.theironyard.repositories;

import com.theironyard.entities.Todo;
import com.theironyard.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by sparatan117 on 1/9/17.
 */
public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> findByUser(User user);
}
