package org.launchcode.lifeorganizer.controllers;

import org.launchcode.lifeorganizer.data.DefaultTaskRepository;
import org.launchcode.lifeorganizer.data.TagRepository;
import org.launchcode.lifeorganizer.data.UserRepository;
import org.launchcode.lifeorganizer.models.DefaultTask;
import org.launchcode.lifeorganizer.models.Tag;
import org.launchcode.lifeorganizer.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("admin")
public class AdminController extends BaseController{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DefaultTaskRepository defaultTaskRepository;

    @Autowired
    private TagRepository tagRepository;

    private static final String userSessionKey = "user";

    @GetMapping("default-create")
    public String displayDefaultCreateForm(Model model) {
        model.addAttribute("title", "Create a new default task");
        model.addAttribute("defaultTask", new DefaultTask());
        model.addAttribute("defaultTasks", defaultTaskRepository.findAll());
        model.addAttribute("btnName","Create");

        return "admin/default-create";
    }

    @PostMapping("default-create")
    public String processDefaultForm(@ModelAttribute @Valid DefaultTask defaultTask, Errors errors, Model model) {
//        User user = authenticationController.getUserFromSession(request.getSession());
        if (errors.hasErrors()) {
            model.addAttribute("title", "Invalid data. Create a new task");
            return "tasks/default";
        }
//       task.setUser(user);
        defaultTaskRepository.save(defaultTask);
        model.addAttribute("defaultTasks", defaultTaskRepository.findAll());

        return "tasks/default";
    }

    @GetMapping("tags")
    public String displayTagsPage(Model model) {
        model.addAttribute(new Tag());
        model.addAttribute("tags", tagRepository.findAll());
        return "admin/tags";
    }

    @PostMapping("tags")
    public String processTags(@ModelAttribute @Valid Tag tag, Errors errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            return "admin/tags";
        }
        tagRepository.save(tag);

        return "redirect:" + request.getHeader("Referer");
    }
}
