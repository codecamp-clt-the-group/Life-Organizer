package org.launchcode.lifeorganizer.controllers;

import org.launchcode.lifeorganizer.data.DefaultTaskRepository;
import org.launchcode.lifeorganizer.data.TagRepository;
import org.launchcode.lifeorganizer.data.TaskRepository;
import org.launchcode.lifeorganizer.data.UserRepository;
import org.launchcode.lifeorganizer.models.DefaultTask;
import org.launchcode.lifeorganizer.models.Tag;
import org.launchcode.lifeorganizer.models.Task;
import org.launchcode.lifeorganizer.models.TaskPriority;
import org.launchcode.lifeorganizer.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.Date;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.launchcode.lifeorganizer.controllers.TasklistController.getLoggedUser;

@Controller
@RequestMapping("tasks")
public class TaskController extends BaseController{

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DefaultTaskRepository defaultTaskRepository;

    @Autowired
    private TagRepository tagRepository;

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
        model.addAttribute("tags", tagRepository.findAll());
        model.addAttribute("title", "Create new task");
        Date date = new Date();
        Task task = new Task();
        task.setDueDate(date);
        model.addAttribute("task", task);
        model.addAttribute("btnName","Create");
        return "tasks/create";
    }


    @PostMapping("create")
    public String processCreateForm(
            @ModelAttribute @Valid Task task,
            Errors errors,
            @RequestParam(required = false) List<Integer> tags,
            HttpServletRequest request,
            Model model) {
        User user = authenticationController.getUserFromSession(request.getSession());

        // check if any tags selected
        if (tags != null) {
            //stream tags into a list
            List<Tag> selectedTags = StreamSupport
                    .stream(tagRepository.findAllById(tags).spliterator(), false)
                    .collect(Collectors.toList());

            //set the tags for the task
            task.setTags(selectedTags);
        }

        if (errors.hasErrors()) {
            model.addAttribute("tags", tagRepository.findAll());
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
        User user = authenticationController.getUserFromSession(request.getSession());

        // find the requested task
        Optional<Task> task = taskRepository.findById(id);

        // check if the user owns that task
        if (task.isPresent() && task.get().getUser().getId() == user.getId()) {
            Task newTask = task.get();

            // set complete and save
            newTask.setComplete();
            taskRepository.save(newTask);
        }
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
            model.addAttribute("tags", tagRepository.findAll());
            model.addAttribute("task", task.get());
            model.addAttribute("title","Edit Task: " + task.get().getName());
            model.addAttribute("btnName","Save Changes");
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
            model.addAttribute("tags", tagRepository.findAll());
            model.addAttribute("title","Edit Task: " + requestedTask.get().getName());
            model.addAttribute("btnName","Save Changes");
            return "tasks/create";
        }

        // check if the user owns that task
        if (requestedTask.isPresent()) { // && requestedTask.get().getUser().getId() == user.getId()
            // process the form to edit the task
            requestedTask.get().setName(task.getName());
            requestedTask.get().setTimeRequired(task.getTimeRequired());
            requestedTask.get().setTags(task.getTags());
            requestedTask.get().setPriority(task.getPriority());
            requestedTask.get().setDueDate(task.getDueDate());
            taskRepository.save(requestedTask.get());
            return "redirect:/tasks";
        }

        return "redirect:";
    }

    @PostMapping("delete")
    public String processDelete(@RequestParam(required = false) int id, HttpServletRequest request) {
        User user = authenticationController.getUserFromSession(request.getSession());

        // find the requested task
        Optional<Task> task = taskRepository.findById(id);

        // check if the user owns that task
        if (task.isPresent() && task.get().getUser().getId() == user.getId()) {
            // Remove
            taskRepository.deleteById(id);
        }
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("default")
    public String displayDefaultForm(Model model) {
        model.addAttribute("title", "Default tasks");
        model.addAttribute("defaultTasks", defaultTaskRepository.findAll());
        return "tasks/default";
    }

    @PostMapping("convert-default-task")
    public String processConvertDefaultTask(@RequestParam(required = false) int id,HttpServletRequest request, Model model) {
        Date newDate = new Date();
        Task newTask = new Task(defaultTaskRepository.findById(id).get().getName(), defaultTaskRepository.findById(id).get().getTimeRequired(), false, TaskPriority.LOW, newDate);
        User user = authenticationController.getUserFromSession(request.getSession());

        newTask.addTags(defaultTaskRepository.findById(id).get().getTags());

        newTask.setUser(user);
        taskRepository.save(newTask);
        return "redirect:";
    }
}
