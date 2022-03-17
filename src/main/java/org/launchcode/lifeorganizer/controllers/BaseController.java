package org.launchcode.lifeorganizer.controllers;

import org.launchcode.lifeorganizer.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.persistence.MappedSuperclass;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@MappedSuperclass
public abstract class BaseController {

    @Autowired
    AuthenticationController authenticationController;

    @ModelAttribute
    private void SetUserInModel(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);

        model.addAttribute("user", user);
    }
}
