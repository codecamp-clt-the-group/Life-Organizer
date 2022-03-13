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
        model.addAttribute("user",current);
        OptionFormDTO newOption = new OptionFormDTO();
        newOption.setEmail(current.getEmail());
        newOption.setFirstName(current.getFirstName());
        newOption.setLastName(current.getLastName());
        newOption.setUserName(current.getUserName());
        model.addAttribute("optionFormDTO",newOption);
        return "profile/options";
    }

    @PostMapping("options")
    public String processDisplayChangeForm(@ModelAttribute @Valid OptionFormDTO optionFormDTO, Errors errors, HttpServletRequest request, Model model){
        User current = authenticationController.getUserFromSession(request.getSession());
        User toUpdate = userRepository.findById(current.getId()).get();

        if(!current.verifyPassword(optionFormDTO.getPwdHash())){
            errors.rejectValue("pwdHash", "pwdHash.invalid", "Incorrect password");
        }
        if(current.verifyPassword(optionFormDTO.getNewPassword())){
            errors.rejectValue("pwdHash", "pwdHash.invalid", "New password cannot be the same as the current password");
        }
        String newPass = optionFormDTO.getNewPassword();
        String verify = optionFormDTO.getVerifyPassword();
        if(!newPass.equals(verify)){
            errors.rejectValue("newPassword", "newPassword.mismatch", "Passwords must match");
        }
        model.addAttribute("title", "Options");
        if (errors.hasErrors()) {
            return "profile/options";
        }
        toUpdate.setPwdHash(newPass);
        userRepository.save(toUpdate);
        model.addAttribute("msg","Password changed successfully");
        return "profile/options";
    }
}
