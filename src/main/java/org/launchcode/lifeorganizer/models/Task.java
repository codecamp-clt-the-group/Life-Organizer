package org.launchcode.lifeorganizer.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Task {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private User user;

    @ManyToMany(mappedBy = "tasks")
    private List<Tasklist> tasklists = new ArrayList<>();

    @NotEmpty(message = "Task must not be empty.")
    @Size(min = 3, max = 200, message = "Task must be between 3 and 200 characters.")
    private String name;

    @PositiveOrZero(message = "The time has to be positive or 0.")
    private int timeRequired;

    boolean isComplete = false;

    public Task () {}

    public Task(String name, int timeRequired, boolean isComplete) {
        this.name = name;
        this.timeRequired = timeRequired;
        this.isComplete = isComplete;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete() {
        if (this.isComplete) {
            this.isComplete = false;
        } else {
            this.isComplete = true;
        }
    }

    public int getTimeRequired() {
        return timeRequired;
    }

    public void setTimeRequired(int timeRequired) {
        this.timeRequired = timeRequired;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public List<Tasklist> getTasklists() {
        return tasklists;
    }

    public void setTasklists(List<Tasklist> tasklists) {
        this.tasklists = tasklists;
    }
}