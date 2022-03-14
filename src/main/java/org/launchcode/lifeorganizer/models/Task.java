package org.launchcode.lifeorganizer.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Task {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private User user;

    @NotEmpty
    @Size(min = 3, max = 200, message = "Task must be between 3 and 200 characters.")
    private String name;

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

}