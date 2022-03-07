package org.launchcode.lifeorganizer.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Entity
public class User {

    @Id
    @GeneratedValue
    private int id;

    @NotBlank(message = "Username required")
    @Size(min = 6, max = 20)
    private String userName;

    @NotBlank(message = "Password required")
    @Size(min = 6)
    private String password;

    @NotBlank(message = "Name required")
    @Size(max = 25)
    private String firstName;

    @NotBlank(message = "Last name required")
    @Size(max = 50)
    private String lastName;

    @Email
    @NotBlank(message = "Email required")
    private String email;

    @NotBlank(message = "passwords must match")
    @Transient
    private String verifyPassword;

    //no-args-constructor
    public User() {
    }

    public User(String userName, String password, String firstName, String lastName, String email, String verifyPassword) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.verifyPassword = verifyPassword;
    }

    public int getId() {
        return id;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean verifyPassword() {
        return this.verifyPassword.equals(this.password);
    }
}
