package org.launchcode.lifeorganizer.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.PERSIST;

@Entity
public class Tasklist {

    @Id
    @GeneratedValue
    private int id;

    @NotEmpty
    @Size(min = 3, max = 200, message = "Task must be between 3 and 200 characters.")
    private String name;

    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(
            name = "tasklist_task",
            joinColumns = @JoinColumn(name = "tasklist_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id"))
    private List<Task> tasks = new ArrayList<>();

    public Tasklist () {}

    public int getId() {
        return id;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
