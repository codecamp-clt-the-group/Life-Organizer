package org.launchcode.lifeorganizer.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Tasklist {

    @Id
    @GeneratedValue
    private int id;

    private List<Task> taskList;

    public Tasklist () {}

    public int getId() {
        return id;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
