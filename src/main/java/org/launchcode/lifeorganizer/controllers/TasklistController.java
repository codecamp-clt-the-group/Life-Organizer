package org.launchcode.lifeorganizer.controllers;

import org.launchcode.lifeorganizer.data.TaskRepository;
import org.launchcode.lifeorganizer.data.TasklistRepository;
import org.launchcode.lifeorganizer.data.UserRepository;
import org.launchcode.lifeorganizer.models.Task;
import org.launchcode.lifeorganizer.models.Tasklist;
import org.launchcode.lifeorganizer.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("tasklist")
public class TasklistController extends BaseController{

    @Autowired
    private TasklistRepository tasklistRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String userSessionKey = "user";

    public User getUserFromSession(HttpSession session) {
        return getLoggedUser(session, userSessionKey, userRepository);
    }

    static User getLoggedUser(HttpSession session, String userSessionKey, UserRepository userRepository) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);
        if (userId == null) {
            return null;
        }
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return null;
        }
        return user.get();
    }

    @GetMapping("")
    public String displayIndex(Model model, HttpServletRequest session) {
        model.addAttribute("title", "My Tasklists");
        User user = getUserFromSession(session.getSession());

        // find all tasklists by user ID
        model.addAttribute("tasklists", tasklistRepository.findAllByUserId(user.getId()));

        return "tasklist/index";
    }

    @GetMapping("generator")
    public String displayForm(Model model) {
        model.addAttribute("title", "Generate Task List");
        return "tasklist/generator";
    }

    @PostMapping("generator")
    public String processForm(@RequestParam int timeAvailable,HttpServletRequest request, Model model) {
        User user = authenticationController.getUserFromSession(request.getSession());


        // check if the time variable if valid
        if (Math.signum(timeAvailable) != 1) {
            model.addAttribute("error", "No tasks available for your timeframe of " + timeAvailable + " minutes.");
            return "tasklist/generator";
        }

        // find all tasks owned by user
        Iterable<Task> allTasks = taskRepository.findAllByUserId(user.getId());

        // sort the tasks and add available that are not on any tasklists
        List<Task> availableTasks = new ArrayList<>();
        for (Task task : allTasks) {
            if (task.getTasklists().size() == 0) {
                availableTasks.add(task);
            }
        }
        model.addAttribute("allTasks", availableTasks);

        // generate suggested tasks for available time allocation
        List<Task> suggestedTasks = new ArrayList<>();
        for (Task task : availableTasks) {
            if (task.getTimeRequired() > 0 && timeAvailable > task.getTimeRequired()) {
                suggestedTasks.add(task);
                timeAvailable = timeAvailable - task.getTimeRequired();
            }
        }

        // if there is any tasks fit in allocated available time
        if (suggestedTasks.size() > 0) {
            model.addAttribute("suggestedTasks", suggestedTasks);

        }
        model.addAttribute("fillTask","fill");
        return "tasklist/generator";
    }

    @GetMapping("{id}")
    public String displayTaskList(Model model, @PathVariable int id, HttpServletRequest session) {
        model.addAttribute("title", "Generated Task List");
        User user = getUserFromSession(session.getSession());

        // find the tasklist by id
        Optional<Tasklist> tasklist = tasklistRepository.findById(id);

        // checking if the user owns that tasklist
        if (tasklist.isPresent() && tasklist.get().getUser().getId() == user.getId()) {
            model.addAttribute("tasks", tasklist.get().getTasks());
            model.addAttribute("tasklist", tasklist.get().getName());
            return "tasklist/list";
        }

        // if not, redirect to tasklist index
        return "redirect:";
    }

    @PostMapping("create")
    public String createTasklist(@ModelAttribute @Valid Tasklist tasklist,
                                 Errors errors,
                                 @RequestParam(required = false) int[] taskIds,
                                 Model model,
                                 HttpServletRequest request) {
        User user = authenticationController.getUserFromSession(request.getSession());


        // if errors or task_ids not provided, redirect to tasklist index
        if (errors.hasErrors() || taskIds==null) {
            return "redirect:generator";
        }

        // remove duplications from task_ids
        List<Integer> ids = new ArrayList<>();
        for (int c : taskIds) {
            if (!ids.contains(c)) {
                ids.add(c);
            }
        }

        // find all tasks by ids
        Iterable<Task> selectedTasks = taskRepository.findAllById(ids);

        // check if the provided ids are owned by the user and not in another tasklist yet
        List<Task> ownedTasks = new ArrayList<>();
        for (Task task : selectedTasks) {
            if (task.getUser().getId() == user.getId() && task.getTasklists().size() == 0) {
                ownedTasks.add(task);
            }
        }

        // set tasks into the tasklist
        tasklist.setTasks(ownedTasks);

        // set user for the tasklist
        tasklist.setUser(user);

        // save
        tasklistRepository.save(tasklist);
        return "redirect:";
    }
}
