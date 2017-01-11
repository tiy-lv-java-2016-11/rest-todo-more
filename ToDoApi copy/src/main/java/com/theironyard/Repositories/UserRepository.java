package com.theironyard.Repositories;

import com.theironyard.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by darionmoore on 1/9/17.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String userName);
    User findByPassHash(String password);
    List<User> findAllById(User user);
}
