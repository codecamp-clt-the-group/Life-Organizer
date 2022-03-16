package org.launchcode.lifeorganizer.controllers;

import org.launchcode.lifeorganizer.models.*;
import org.launchcode.lifeorganizer.data.TasklistRepository;
import org.launchcode.lifeorganizer.data.UserRepository;
import org.launchcode.lifeorganizer.models.dto.OptionFormDTO;
import org.launchcode.lifeorganizer.models.dto.SignupFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("profile")
public class ProfileController {
    @Autowired
    TasklistRepository tasklistRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationController authenticationController;

    @GetMapping
    public String displayProfile(HttpServletRequest request, Model model){
        User current = authenticationController.getUserFromSession(request.getSession());
        List<Tasklist> taskLst = (List<Tasklist>) tasklistRepository.findAll();
        model.addAttribute("TaskLists", taskLst);
        model.addAttribute("user",current);
        return "profile/index";
    }

    @GetMapping("options")
    public String displayChangeForm(Model model, HttpServletRequest request){
        User current = authenticationController.getUserFromSession(request.getSession());
        model.addAttribute("title","Options");
        OptionFormDTO newOption = new OptionFormDTO();
        newOption.setEmail(current.getEmail());
        newOption.setPwdHash(current.getPwdHash());
        newOption.setFirstName(current.getFirstName());
        newOption.setLastName(current.getLastName());
        newOption.setUserName(current.getUserName());
        model.addAttribute("optionFormDTO",newOption);
        model.addAttribute("user",current);
        return "profile/options";
    }

    @PostMapping("options")
    public String processDisplayChangeForm(@ModelAttribute @Valid OptionFormDTO optionFormDTO, Errors errors, HttpServletRequest request, Model model){
        User current = authenticationController.getUserFromSession(request.getSession());
        User toUpdate = userRepository.findById(current.getId()).get();

        String curPass = optionFormDTO.getCurrentPassword();
        String newPass = optionFormDTO.getNewPassword();
        String verify = optionFormDTO.getVerifyPassword();
        String newFirst = optionFormDTO.getFirstName();
        String newLast = optionFormDTO.getLastName();
        String newEmail = optionFormDTO.getEmail();


        if(!newPass.equals("") || !verify.equals("") || !curPass.equals("")) {
            if (!current.verifyPassword(curPass)) {
                errors.rejectValue("currentPassword", "currentPassword.invalid", "Incorrect password");
            }
            if (newPass.length() >= 6) {
                if (current.verifyPassword(newPass)) {
                    errors.rejectValue("newPassword", "newPassword.invalid", "New password cannot be the same as the current password");
                }
            } else {
                errors.rejectValue("newPassword", "newPassword.invalid", "Password must be at least 6 characters");
            }
            if (!newPass.equals(verify)) {
                errors.rejectValue("verifyPassword", "verifyPassword.mismatch", "Passwords must match");
            }
        }

        model.addAttribute("title", "Options");
        if (errors.hasErrors()) {
            return "profile/options";
        }

        if(!newPass.equals("")){
            toUpdate.setPwdHash(newPass);
        }

        toUpdate.setFirstName(newFirst);
        toUpdate.setLastName(newLast);
        toUpdate.setEmail(newEmail);


        userRepository.save(toUpdate);
        model.addAttribute("msg","Profile updated successfully");
        return "profile/options";
    }
}
