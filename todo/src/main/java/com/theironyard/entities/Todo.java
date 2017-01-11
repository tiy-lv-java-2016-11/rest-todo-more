package com.theironyard.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Created by sparatan117 on 1/9/17.
 */
@Entity
public class Todo {
    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    @Size(min = 3, max = 12, message = "title must be between 3 and 12")
    private String title;

    @Column(nullable = false)
    @Size(min = 2, max = 100, message = "description must be between 2 and 100")
    private String description;

    @Column(nullable = false)
    @Size(min = 2, max = 30, message = "due date must between 2 and 30")
    private String dueDate;

    @ManyToOne
    private User user;

    public Todo() {
    }

    public Todo(String title, String description, String dueDate, User user) {
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
