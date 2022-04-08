package org.launchcode.lifeorganizer.controllers;

import org.launchcode.lifeorganizer.data.UserRepository;
import org.launchcode.lifeorganizer.models.Mail;
import org.launchcode.lifeorganizer.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Random;

@Controller
@Component
public class MailController implements WebMvcConfigurer {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;

    public void sendForgotPassword(String to, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("info@thegroup.com");
        message.setSubject("Forgot Password");
        message.setText(text);
        mailSender.send(message);
    }

    @GetMapping("forgot")
    public String displayForgot(Model model){
        model.addAttribute(new Mail());
        model.addAttribute("title", "Forgot Password");
        return "forgot";
    }
    @PostMapping("forgot")
    public String processForgot(@ModelAttribute @Valid Mail mail, HttpServletRequest request, Errors errors, Model model) throws AddressException, MessagingException, IOException {
        User user = userRepository.findByEmail(mail.getEmail());
        if (userRepository.findByEmail(mail.getEmail()) == null){
            errors.rejectValue("email", "email.invalid", "The email provided is not on valid");
            model.addAttribute("title", "Errors occur. Please enter a valid email.");
            return "forgot";
        }

        if (errors.hasErrors()) {
            model.addAttribute("msg", "Errors occur. Please enter valid credentials.");
            return "forgot";
        }
        String ranGen = forgotPassword();
        user.setPwdHash(ranGen);
        String text = user.getFirstName() + " " + user.getLastName()+ ", here is a temporary password to login.\n\t" + ranGen + "\nUse this to login, but make sure to change it!";
        mail.sendMessage(user.getEmail(), "Forgot Password", text);
        userRepository.save(user);
        model.addAttribute("msg","A temporary password has been sent to your email.");
        return "redirect:/login";
    }

    public String forgotPassword(){
        String temp = "";
        Random random = new Random();
        int type = 0;
        int charValue = 0;
        for(int i = 0; i < 6;i++){
            type = random.nextInt(2 - 0) + 0;
            switch(type){
                case 0:
                    charValue = random.nextInt(57 - 48) + 48;
                    break;
                case 1:
                    charValue = random.nextInt(90 - 65) + 65;
                    break;
                case 2:
                    charValue = random.nextInt(122 - 97) + 97;
                    break;
                default:
                    break;
            }
            temp += Character.toString((char) charValue);
        }
        return temp;
    }

}
