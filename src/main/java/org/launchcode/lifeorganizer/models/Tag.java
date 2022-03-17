package org.launchcode.lifeorganizer.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tag {

    @Id
    @GeneratedValue
    private int id;

    @NotEmpty(message = "Tag field must not be empty.")
    @Size(min = 3, max = 200, message = "Tag field must be between 3 and 200 characters.")
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Task> tasks = new ArrayList<>();

    @ManyToMany(mappedBy = "tags")
    private List<User> users = new ArrayList<>();

    public Tag () {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
