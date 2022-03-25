package org.launchcode.lifeorganizer.controllers;

import org.launchcode.lifeorganizer.data.DefaultTaskRepository;
import org.launchcode.lifeorganizer.data.TagRepository;
import org.launchcode.lifeorganizer.data.UserRepository;
import org.launchcode.lifeorganizer.models.DefaultTask;
import org.launchcode.lifeorganizer.models.Tag;
import org.launchcode.lifeorganizer.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        model.addAttribute("tags", tagRepository.findAll());
        model.addAttribute("title", "Create a new default task");
        model.addAttribute("defaultTask", new DefaultTask());
        model.addAttribute("btnName","Create");

        return "admin/default-create";
    }

    @PostMapping("default-create")
    public String processDefaultForm(
            @ModelAttribute @Valid DefaultTask defaultTask,
             Errors errors,
             @RequestParam(required = false) List<Integer> tags,
             HttpServletRequest request,
             Model model) {
        if (tags != null) {
            //stream tags into a list
            List<Tag> selectedTags = StreamSupport
                    .stream(tagRepository.findAllById(tags).spliterator(), false)
                    .collect(Collectors.toList());

            //set the tags for the task
            defaultTask.setTags(selectedTags);
        }

        if (errors.hasErrors()) {
            model.addAttribute("title", "Create a new default task. Error occur.");
            model.addAttribute("tags", tagRepository.findAll());
            model.addAttribute("btnName","Create");
            return "admin/default-create";
        }
        defaultTaskRepository.save(defaultTask);
        model.addAttribute("defaultTasks", defaultTaskRepository.findAll());

        return "tasks/default";
    }

    @GetMapping("tags")
    public String displayTagsPage(Model model) {
        model.addAttribute(new Tag());
        model.addAttribute("tags", tagRepository.findAll());
        model.addAttribute("title", "Add a tag");
        return "admin/tags";
    }

    @PostMapping("tags")
    public String processTags(@ModelAttribute @Valid Tag tag, Errors errors, HttpServletRequest request, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("tags", tagRepository.findAll());
            model.addAttribute("title", "Add a tag");
            return "admin/tags";
        }
        tagRepository.save(tag);

        return "redirect:" + request.getHeader("Referer");
    }
}
