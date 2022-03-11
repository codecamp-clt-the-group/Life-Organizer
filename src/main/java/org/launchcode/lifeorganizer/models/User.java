package org.launchcode.lifeorganizer.models;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    @Id
    @GeneratedValue
    private int id;

    @OneToMany
    @JoinColumn(name="user_id")
    private final List<Task> tasks = new ArrayList<>();

    @NotBlank(message = "Username required")
    @Size(min = 6, max = 20)
    private String userName;
    private String firstName;
    private String lastName;
    private String email;

    @NotBlank(message = "Password required")
    @Size(min = 6)
    private String pwdHash;

    //no-args-constructor
    public User() {
    }

    public User(String userName, String firstName, String lastName, String email, String password) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pwdHash = encoder.encode(password);
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwdHash() {
        return pwdHash;
    }

    public void setPwdHash(String password) {
        this.pwdHash = encoder.encode(password);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean verifyPassword(String verifyPassword) {
        return encoder.matches(verifyPassword, pwdHash);
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
