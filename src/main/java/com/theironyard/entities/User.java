package com.theironyard.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.theironyard.utilities.PasswordStorage;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "users")
public class User {
    private static final int EXPIRATION_DAYS = 3;

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String username;

    @Column
    private String email;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(unique = true)
    @JsonIgnore
    private String token;

    @Column(nullable = false)
    @ColumnDefault("'1970-01-01'")
    @JsonIgnore
    private LocalDateTime expiration;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        setTokenAndExpiration();
    }

    public User(String username, String password, String email, String firstName, String lastName) throws PasswordStorage.CannotPerformOperationException {
        this.username = username;
        this.password = PasswordStorage.createHash(password);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        setTokenAndExpiration();
    }

    public String generateToken(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    public String regenerateToken(){
        setTokenAndExpiration();
        return this.token;
    }

    private void setTokenAndExpiration(){
        this.token = generateToken();
        this.expiration = LocalDateTime.now().plus(EXPIRATION_DAYS, ChronoUnit.DAYS);
    }

    public boolean isTokenValid(){
        return this.expiration.isAfter(LocalDateTime.now());
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

    public void setPassword(String password) throws PasswordStorage.CannotPerformOperationException {
        this.password = PasswordStorage.createHash(password);
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
