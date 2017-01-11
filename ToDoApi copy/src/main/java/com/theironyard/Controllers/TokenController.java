package com.theironyard.Controllers;

import com.theironyard.CommandObjects.ValidateLogin;
import com.theironyard.Entities.User;
import com.theironyard.Exceptions.LoginException;
import com.theironyard.Repositories.UserRepository;
import com.theironyard.Utilities.PasswordHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by darionmoore on 1/10/17.
 */
@RestController
public class TokenController {

    @Autowired
    UserRepository userRepository;



    @RequestMapping(path = "/token/", method = RequestMethod.POST)
    public String getToken(@RequestBody ValidateLogin validate) throws Exception {
        User user = userRepository.findByUserName(validate.getUserName());
        if(user == null){
            user = new User(validate.getUserName(), PasswordHash.createHash(validate.getPassword()));
            userRepository.save(user);
        } else if(!PasswordHash.verifyPassword(validate.getPassword(), user.getPassword())){
            throw new LoginException();
        }
        return user.generateToken();
    }

    public String regenerateToken(@RequestBody ValidateLogin validate) throws PasswordHash.InvalidHashException, PasswordHash.CannotPerformOperationException {
        User user = userRepository.findByUserName(validate.getUserName());
        if (user != null) {
            if (!PasswordHash.verifyPassword(validate.getPassword(), user.getPassword())) {
                throw new LoginException();
            }
            Map<String, String> userToken = new HashMap<>();
            userToken.put("token", user.getToken());
            return user.regenerateToken();
        }
        return user.regenerateToken();


    }
}
