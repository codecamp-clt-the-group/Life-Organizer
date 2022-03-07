package org.launchcode.lifeorganizer.controller;
import org.launchcode.lifeorganizer.data.UserRepository;
import org.launchcode.lifeorganizer.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.validation.Valid;

@Controller
@RequestMapping("signup")
public class SignupController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public String displaySignupForm(Model model) {
        model.addAttribute(new User());
        return "signup/signup";
    }

    @PostMapping
    public String processSignupForm(@ModelAttribute @Valid User newUser, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "signup/signup";
        }
        if (newUser.verifyPassword()) {
            userRepository.save(newUser);
            return "./index";
        } else {
            model.addAttribute("errorMessage","passwords must match");
            return "signup/signup";
        }
    }
}
