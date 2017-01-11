package com.theironyard.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by sparatan117 on 1/9/17.
 */
@Entity
@Table(name = "users")
public class User {
    private static final int EXPIRATION_DAYS = 3;

    @Id
    @GeneratedValue
    @Min(0)
    private int id;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 50, message = "username must be between 3 and 12")
    private String username;

    @Column(nullable = false)
    @Size(min = 6, max = 100, message = "email must be between 6 and 20")
    private String email;

    @Column(nullable = false)
    @Size(min = 2, max = 50, message = "first name must be between 2 and 12")
    private String firstName;

    @Column(nullable = false)
    @Size(min = 2, max = 50, message = "last name must be between 2 and 12")
    private String lastName;

    @Column(nullable = false)
    @Size(min = 2, max = 1000, message = "pass is too long")
    private String password;

    @Column(nullable = true, unique = true)
    @JsonIgnore
    private String token;

    @Column(nullable = false)
    @ColumnDefault("'1970-01-01'")
    private LocalDateTime expiration;

    public User() {
        setTokenAndExpiration();
    }

    public User(String username, String email, String firstName, String lastName, String password) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        setTokenAndExpiration();
    }
    public User(String username, String password){
        this.username = username;
        this.password = password;

    }


    private void setTokenAndExpiration(){
        this.token = generateToken();
        this.expiration = LocalDateTime.now().plus(EXPIRATION_DAYS, ChronoUnit.DAYS);
    }

    public String generateToken(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    public boolean isTokenValid(){
        return expiration.isAfter(LocalDateTime.now());
    }

    public String regenerate(){
        setTokenAndExpiration();
        return token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }
}
