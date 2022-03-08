package org.launchcode.lifeorganizer.controllers;

import org.launchcode.lifeorganizer.data.TaskRepository;
import org.launchcode.lifeorganizer.data.TasklistRepository;
import org.launchcode.lifeorganizer.models.Task;
import org.launchcode.lifeorganizer.models.Tasklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("tasklist")
public class TasklistController {

    @Autowired
    private TasklistRepository tasklistRepository;

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("generator")
    public String displayForm(Model model) {
        model.addAttribute("title", "Generate Task List");
        return "tasklist/generator";
    }

    @PostMapping("generator")
    public String processForm(@RequestParam int timeAvailable, Model model) {
        model.addAttribute("title", "Generated Task List");
        Iterable<Task> allTasks = taskRepository.findAll();
        List<Task> timeRequiredTasks=new ArrayList<>();
        List<Task> selectedTasks=new ArrayList<>();
        for (Task task : allTasks) {
            if (task.getTimeRequired()>0) {
                timeRequiredTasks.add(task);
            }
        }
        for (Task task : timeRequiredTasks) {
            if (timeAvailable>task.getTimeRequired()) {
                selectedTasks.add(task);
                timeAvailable-=task.getTimeRequired();
            }
        }
//        Tasklist listOfTasks = null;
//        listOfTasks.setTasks(selectedTasks);
//        tasklistRepository.save(listOfTasks);

        if (selectedTasks.size()>0) {
          tasklistRepository.save(new Tasklist());
//          for (Task task : selectedTasks) {
//              task.setTasklist();
//          }
            taskRepository.saveAll(selectedTasks);
        }
        return "tasklist/list";
    }


    @GetMapping("tasklist/list")
    public String displayTaskList(Model model) {
        model.addAttribute("title", "Generate Task List");
        return "tasklist/list";
    }
}
