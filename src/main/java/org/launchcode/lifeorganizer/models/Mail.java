package org.launchcode.lifeorganizer.models;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Properties;


public class Mail {

    @NotNull(message = "Please enter an email.")
    @NotBlank(message = "Please enter an email.")
    private String email;

    private static final String noReply = "noreply@thegroup.com";

    @Bean
    public JavaMailSender getJavaMailSender2() { // Used for testing if an email has been sent.
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.mailtrap.io");
        mailSender.setPort(2525);

        mailSender.setUsername("4b12cc21c54a47");
        mailSender.setPassword("b28dddef3e4a8d");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
    @Bean
    public JavaMailSender getJavaMailSender() { // Used for actually sending the email.
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.office365.com");
        mailSender.setPort(587);

        mailSender.setUsername("thegroup.clt@outlook.com");
        mailSender.setPassword("Launchcode");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    public void sendMessage(String sendTo, String subject, String text) throws AddressException, MessagingException, IOException {

        MimeMessage message = getJavaMailSender().createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false);
        helper.setFrom("thegroup.clt@outlook.com");
        helper.setTo(new InternetAddress(sendTo));
        helper.setSubject(subject);
        helper.setText(text);
        getJavaMailSender().send(message);

//        MimeMessage message = getJavaMailSender2().createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, false);
//        helper.setFrom(noReply);
//        helper.setTo(new InternetAddress(sendTo));
//        helper.setSubject(subject);
//        helper.setText(text);
//        getJavaMailSender().send(message);

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
