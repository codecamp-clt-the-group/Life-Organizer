package org.launchcode.lifeorganizer.models;

import javax.persistence.*;
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

    private int timeRequired;

    private boolean isComplete = false;

//    @ManyToOne
//    @JoinColumn(name = "tasklist_id")
//    private Tasklist tasklist;
    private int tasklist_id = 0;

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

    public int getTimeRequired() {
        return timeRequired;
    }

    public void setTimeRequired(int timeRequired) {
        this.timeRequired = timeRequired;
    }

    public int getTasklist_id() {
        return tasklist_id;
    }

    public void setTasklist_id(int tasklist_id) {
        this.tasklist_id = tasklist_id;
    }

    //    public Tasklist getTasklist() {
//        return tasklist;
//    }
//
//    public void setTasklist(Tasklist tasklist) {
//        this.tasklist = tasklist;
//    }
}