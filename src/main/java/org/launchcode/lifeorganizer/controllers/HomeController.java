package org.launchcode.lifeorganizer.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController extends BaseController{

    @GetMapping("")
    public String displayHomePage(Model model) {

        return "index";
    }
}
