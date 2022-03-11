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

    @GetMapping("")
    public String displayIndex(Model model) {
        model.addAttribute("title", "My Tasklists");
        model.addAttribute("tasklists", tasklistRepository.findAll());

        return "tasklist/index";
    }

    @GetMapping("generator")
    public String displayForm(Model model) {
        model.addAttribute("title", "Generate Task List");
        return "tasklist/generator";
    }

    @PostMapping("generator")
    public String processForm(@RequestParam int timeAvailable, Model model) {
//        Iterable<Task> allTasks = taskRepository.findAll();
        Iterable<Task> allTasks = taskRepository.findTasksForTasklist();

        List<Task> suggestedTasks=new ArrayList<>();
        for (Task task : allTasks) {
            if (task.getTimeRequired() > 0 && timeAvailable > task.getTimeRequired()) {
                suggestedTasks.add(task);
                timeAvailable = timeAvailable - task.getTimeRequired();
            }
        }

        model.addAttribute("allTasks", allTasks);


        if (suggestedTasks.size() > 0) {

            model.addAttribute("suggestedTasks", suggestedTasks);
        }
//        else {
//            model.addAttribute("title", "Generate Task List");
//            model.addAttribute("error", "No tasks available for your timeframe of " + timeAvailable + ".");
//            return "tasklist/generator";
//        }
        return "tasklist/suggested";
    }

    @GetMapping("{id}")
    public String displayTaskList(Model model, @PathVariable int id) {
        model.addAttribute("title", "Generated Task List");
        Optional<Tasklist> tasklist = tasklistRepository.findById(id);
        model.addAttribute("tasks", tasklist.get().getTasks());
        return "tasklist/list";
    }

    @PostMapping("create")
    public String createTasklist(@RequestParam int[] taskIds, Model model) {
        System.out.println(taskIds.toString());

        List<Integer> ids = new ArrayList<>();

        for(int c : taskIds) {
            if(!ids.contains(c)) {
              ids.add(c);
            }
        }

        Tasklist tasklist = new Tasklist();
        List<Task> selectedTasks = (List<Task>) taskRepository.findAllById(ids);
        tasklist.setTasks(selectedTasks);
        tasklistRepository.save(tasklist);
        return "redirect:";
    }
}
