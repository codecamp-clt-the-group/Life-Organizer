package org.launchcode.lifeorganizer.controllers;

import org.launchcode.lifeorganizer.data.DefaultTaskRepository;
import org.launchcode.lifeorganizer.data.TaskRepository;
import org.launchcode.lifeorganizer.data.UserRepository;
import org.launchcode.lifeorganizer.models.DefaultTask;
import org.launchcode.lifeorganizer.models.Task;
import org.launchcode.lifeorganizer.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

import static org.launchcode.lifeorganizer.controllers.TasklistController.getLoggedUser;

@Controller
@RequestMapping("tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DefaultTaskRepository defaultTaskRepository;

    private static final String userSessionKey = "user";

    public User getUserFromSession(HttpSession session) {
        return getLoggedUser(session, userSessionKey, userRepository);
    }

    @GetMapping
    public String displayAllTasks(Model model, HttpServletRequest session) {
        model.addAttribute("title", "All Tasks");
        User user = getUserFromSession(session.getSession());
        model.addAttribute("tasks", taskRepository.findAllByUserId(user.getId()));
        return "tasks/index";
    }

    @GetMapping("create")
    public String displayForm(Model model) {
        model.addAttribute("title", "Create new task");
        model.addAttribute("task", new Task());
        model.addAttribute("btnName","Create");
        return "tasks/create";
    }


    @PostMapping("create")
    public String processCreateForm(@ModelAttribute @Valid Task task, Errors errors, HttpServletRequest request, Model model) {
        User user = authenticationController.getUserFromSession(request.getSession());
        if (errors.hasErrors()) {
            model.addAttribute("title", "Create new task");
            model.addAttribute("btnName","Create");
            return "tasks/create";
        }
        task.setUser(user);
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

    @GetMapping("{id}/edit")
    public String displayEditForm(@PathVariable int id, HttpServletRequest request, Model model) {
        User user = authenticationController.getUserFromSession(request.getSession());

        // find the requested task
        Optional<Task> task = taskRepository.findById(id);

        // check if the user owns that task
        if (task.isPresent() && task.get().getUser().getId() == user.getId()) {
        // return the form to edit the task
            model.addAttribute("task", task.get());
            model.addAttribute("title","Edit Task");
            model.addAttribute("btnName","edit");
            return "tasks/create";
        }
        // if user doesn't own the task, redirect to tasks/index
        return "redirect:";
    }

    @PostMapping("{id}/edit")
    public String processEditForm(@ModelAttribute @Valid Task task, Errors errors, HttpServletRequest request, Model model, @PathVariable int id) {
        User user = authenticationController.getUserFromSession(request.getSession());

        // find the requested task
        Optional<Task> requestedTask = taskRepository.findById(id);

        if (errors.hasErrors() || requestedTask.get().getUser().getId() != user.getId()) {
            model.addAttribute("title","Edit Task");
            model.addAttribute("btnName","edit");
            return "tasks/create";
        }

        // check if the user owns that task
        if (requestedTask.isPresent()) { // && requestedTask.get().getUser().getId() == user.getId()
            // process the form to edit the task
            requestedTask.get().setName(task.getName());
            requestedTask.get().setTimeRequired(task.getTimeRequired());
            taskRepository.save(requestedTask.get());
            return "redirect:/tasks";
        }

        return "redirect:";
    }

    @PostMapping("delete")
    public String processDelete(@RequestParam(required = false) int id) {
        if (id > 0) {
            taskRepository.deleteById(id);
        }
        return "redirect:";
    }

    @GetMapping("default")
    public String displayDefaultForm(Model model) {
        model.addAttribute("title", "Create a new default task");
     //   model.addAttribute("defaultTask", new DefaultTask());
        model.addAttribute("defaultTasks", defaultTaskRepository.findAll());
        return "tasks/default";
    }

    @GetMapping("default-create")
    public String displayDefaultCreateForm(Model model) {
        model.addAttribute("title", "Create a new default task");
        model.addAttribute("defaultTask", new DefaultTask());
        model.addAttribute("defaultTasks", defaultTaskRepository.findAll());
        return "tasks/default-create";
    }

    @PostMapping("default-create")
    public String processDefaultForm(@ModelAttribute @Valid DefaultTask defaultTask, Errors errors, HttpServletRequest request, Model model) {
//        User user = authenticationController.getUserFromSession(request.getSession());
        if (errors.hasErrors()) {
            model.addAttribute("title", "Invalid data. Create a new task");
            return "tasks/default";
        }
//        task.setUser(user);
        defaultTaskRepository.save(defaultTask);
        model.addAttribute("defaultTasks", defaultTaskRepository.findAll());

        return "tasks/default";
    }

    @PostMapping("convert-default-task")
    public String processConvertDefaultTask(@RequestParam(required = false) int id,HttpServletRequest request, Model model) {
        Task newTask = new Task(defaultTaskRepository.findById(id).get().getName(), defaultTaskRepository.findById(id).get().getTimeRequired(), false);
        User user = authenticationController.getUserFromSession(request.getSession());

        newTask.setUser(user);
        taskRepository.save(newTask);
        return "redirect:";
    }
}
