package com.theironyard.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "todos")
public class Todo {
    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(nullable = false)
    private LocalDate dueDate;

    @ManyToOne
    private User user;


    public Todo() {
    }

    public Todo(String title, String description, String dueDate, User user) {
        this.title = title;
        this.description = description;
        this.dueDate = LocalDate.parse(dueDate);
        this.user = user;
    }

    public Todo(String title, String description, LocalDate dueDate, User user) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate(){
        return this.dueDate.toString();
    }

//    public LocalDate getDueDate() {
//        return dueDate;
//    }

    public void setDueDate(String dueDate){
        this.dueDate = LocalDate.parse(dueDate);
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
