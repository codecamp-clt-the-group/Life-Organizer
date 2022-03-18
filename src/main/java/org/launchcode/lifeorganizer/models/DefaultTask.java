package org.launchcode.lifeorganizer.models;

import org.launchcode.lifeorganizer.data.DefaultTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class DefaultTask{

    @Id
    @GeneratedValue
    private Integer id;

    @NotEmpty
    private String name;

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
