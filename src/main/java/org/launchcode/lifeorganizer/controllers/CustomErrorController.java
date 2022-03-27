package org.launchcode.lifeorganizer.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null){
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("error",HttpStatus.NOT_FOUND);
                model.addAttribute("errorTitle",HttpStatus.NOT_FOUND.value() + " " + HttpStatus.NOT_FOUND.name());
                model.addAttribute("errorMsg","The page/content you are looking for is unavailable.");
            } else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("error", HttpStatus.INTERNAL_SERVER_ERROR);
                model.addAttribute("errorTitle",HttpStatus.INTERNAL_SERVER_ERROR.value() + " " + HttpStatus.INTERNAL_SERVER_ERROR.name());
                model.addAttribute("errorMsg","Oops! There is an issue on the server. Try again later.");
            }
        }
        return "error";
    }
}
