package org.launchcode.lifeorganizer.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class DefaultTask{

    @Id
    @GeneratedValue
    private Integer id;

    @NotEmpty(message = "Task must not be empty.")
    @Size(min = 3, message = "Task name must be at least 3 characters long")
    private String name;

    @PositiveOrZero(message = "The time has to be positive or 0.")
    private int timeRequired;

    @ManyToMany
    @JoinTable(
            name = "defaultTask_tag",
            joinColumns = @JoinColumn(name = "defaultTask_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags = new ArrayList<>();

    public DefaultTask () {}

    public DefaultTask(int id, String name, int timeRequired) {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeRequired() {
        return timeRequired;
    }

    public void setTimeRequired(int timeRequired) {
        this.timeRequired = timeRequired;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

//    public Task createTask() {
//        Date newDate = new Date();
//        Task newTask = new Task(defaultTaskRepository.findById(id).get().getName(), defaultTaskRepository.findById(id).get().getTimeRequired(), false, TaskPriority.LOW, newDate);
//        return newTask;
//    }
}
