package org.launchcode.lifeorganizer.controllers;

import org.launchcode.lifeorganizer.data.UserRepository;
import org.launchcode.lifeorganizer.models.User;
import org.launchcode.lifeorganizer.models.dto.LoginFormDTO;
import org.launchcode.lifeorganizer.models.dto.SignupFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

import static org.launchcode.lifeorganizer.controllers.TasklistController.getLoggedUser;


@Controller
public class AuthenticationController{

    @Autowired
    UserRepository userRepository;

    static final String SIGN_UP_TITLE = "Sign-Up";

    private static final String userSessionKey = "user";

    public User getUserFromSession(HttpSession session) {
        return getLoggedUser(session, userSessionKey, userRepository);
    }

    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
    }

    @GetMapping("/signup")
    public String displaySignupForm(Model model) {
        model.addAttribute(new SignupFormDTO());
        model.addAttribute("title", SIGN_UP_TITLE);
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignupForm(@ModelAttribute @Valid SignupFormDTO signupFormDTO, Errors errors, HttpServletRequest request, Model model) {

        User existingUser = userRepository.findByUserName(signupFormDTO.getUserName());

        if (existingUser != null) {
            errors.rejectValue("userName", "userName.alreadyexists", "A user with that username already exists.");
        }
        User existingEmail = userRepository.findByEmail(signupFormDTO.getEmail());
        if (existingEmail != null) {
            errors.rejectValue("email", "email.alreadyexists", "A user with that email already exists.");
        }
        String password = signupFormDTO.getPwdHash();
        String verifyPassword = signupFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("pwdHash", "pwdHash.mismatch", "Passwords must match");
        }
        if (errors.hasErrors()) {
            model.addAttribute("title", SIGN_UP_TITLE);
            return "signup";
        }
        User newUser = new User(signupFormDTO.getUserName(), signupFormDTO.getFirstName(), signupFormDTO.getLastName(), signupFormDTO.getEmail(), signupFormDTO.getPwdHash());

        userRepository.save(newUser);
        setUserInSession(request.getSession(), newUser);



        return "redirect:";
    }


    @GetMapping("login")
    public String displayLoginForm(Model model) {
        model.addAttribute(new LoginFormDTO());
        return "login";
    }


    @PostMapping("login")
    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO,
                                   Errors errors, HttpServletRequest request,
                                   Model model) {

        if (userRepository.findByUserName(loginFormDTO.getUserName()) == null){
            errors.rejectValue("userName", "userName.invalid", "The username provided is not valid");
            return "login";
        }
        User theUser = userRepository.findByUserName(loginFormDTO.getUserName());

        String password = loginFormDTO.getPwdHash();

        if (!theUser.verifyPassword(password)) {
            errors.rejectValue("pwdHash", "pwdHash.invalid", "Invalid Password");
        }
        if (errors.hasErrors()) {
            return "login";
        }
        setUserInSession(request.getSession(), theUser);

        HttpSession session = request.getSession();
        User user = getUserFromSession(session);

        model.addAttribute("user", user);

        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/login";
    }


}
