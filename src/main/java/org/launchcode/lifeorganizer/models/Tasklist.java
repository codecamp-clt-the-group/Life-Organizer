package org.launchcode.lifeorganizer.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tasklist {

    @Id
    @GeneratedValue
    private int id;

//    @OneToMany(mappedBy = "tasklist")
//    private List<Task> tasks = new ArrayList<>();

    public Tasklist () {}

    public int getId() {
        return id;
    }

//    public List<Task> getTasks() {
//        return tasks;
//    }
//
//    public void setTasks(List<Task> tasks) {
//        this.tasks = tasks;
//    }
}
