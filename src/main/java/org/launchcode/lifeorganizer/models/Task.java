package org.launchcode.lifeorganizer.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
public class Task {

    @Id
    @GeneratedValue
    private int id;

    @NotEmpty
    @Size(min = 3, max = 200, message = "Task must be between 3 and 200 characters.")
    private String name;

    private boolean isComplete = false;

    public Task () {}

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
}