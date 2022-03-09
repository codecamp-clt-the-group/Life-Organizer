package org.launchcode.lifeorganizer.controllers;

import org.launchcode.lifeorganizer.data.TaskRepository;
import org.launchcode.lifeorganizer.data.TasklistRepository;
import org.launchcode.lifeorganizer.models.Task;
import org.launchcode.lifeorganizer.models.Tasklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
//        Iterable<Task> allTasks = taskRepository.findAll();
        Iterable<Task> allTasks = taskRepository.findTasksForTasklist();

        List<Task> selectedTasks=new ArrayList<>();
        for (Task task : allTasks) {
            if (task.getTimeRequired() > 0 && timeAvailable > task.getTimeRequired()) {
                selectedTasks.add(task);
                timeAvailable = timeAvailable - task.getTimeRequired();
            }
        }

        Tasklist tasklist = new Tasklist();

        if (selectedTasks.size() > 0) {
            tasklist.setTasks(selectedTasks);
            tasklistRepository.save(tasklist);
        } else {
            model.addAttribute("title", "Generate Task List");
            model.addAttribute("error", "No tasks available for your timeframe of " + timeAvailable + ".");
            return "tasklist/generator";
        }
        return "redirect:/tasklist/" + tasklist.getId();
    }

    @GetMapping("{id}")
    public String displayTaskList(Model model, @PathVariable int id) {
        model.addAttribute("title", "Generated Task List");
        Optional<Tasklist> tasklist = tasklistRepository.findById(id);
        model.addAttribute("tasks", tasklist.get().getTasks());
        return "tasklist/list";
    }
}
