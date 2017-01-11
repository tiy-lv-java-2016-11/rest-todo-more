package com.theironyard.Entities;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by darionmoore on 1/9/17.
 */
@Entity
@Table(name = "users")
public class User {
    private static final int EXPIRATION_DAYS = 1;

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = true, unique = true)
    private String token;

    @Column(nullable = false)
    @ColumnDefault("'2017-01-10'")
    private LocalDateTime expiration;



    public User() {
        setTokenAndExpiration();

    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User(int id, String userName, String password, String email, String firstName, String lastName) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.token = generateToken();
        setTokenAndExpiration();
    }
//generates new token and sets expiration date
    private void setTokenAndExpiration(){
        this.token = generateToken();
        this.expiration = LocalDateTime.now().plus(EXPIRATION_DAYS, ChronoUnit.DAYS);

    }
//creates a token
    public String generateToken(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(150, random).toString(32);
    }

    public String regenerateToken(){
        setTokenAndExpiration();
        return token;
    }
//validates token
    public boolean validateToken(){
        return expiration.isAfter(LocalDateTime.now());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
