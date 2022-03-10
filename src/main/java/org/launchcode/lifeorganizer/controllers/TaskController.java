package org.launchcode.lifeorganizer.controllers;

import org.launchcode.lifeorganizer.data.TaskRepository;
import org.launchcode.lifeorganizer.models.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public String displayAllTasks(Model model) {
        model.addAttribute("title", "All Tasks");
        model.addAttribute("tasks", taskRepository.findAll());
        return "tasks/index";
    }

    @GetMapping("create")
    public String displayForm(Model model) {
        model.addAttribute("title", "Create a new task");
        model.addAttribute("task", new Task());
        return "tasks/create";
    }

    @PostMapping("create")
    public String processCreateForm(@ModelAttribute @Valid Task task, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Invalid data. Create a new task");
            return "tasks/create";
        }
        taskRepository.save(task);

        return "redirect:";
    }

    @GetMapping("{id}")
    public String toggleIsComplete(@PathVariable int id, HttpServletRequest request) {
        Optional<Task> task = taskRepository.findById(id);
        Task newTask = task.get();
        newTask.setComplete();
        taskRepository.save(newTask);

        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("delete")
    public String processDelete(@RequestParam(required = false) int id) {
        if (id > 0) {
            taskRepository.deleteById(id);
        }
        return "redirect:";
    }


}
