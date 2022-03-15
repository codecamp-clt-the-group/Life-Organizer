package org.launchcode.lifeorganizer.models.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SignupFormDTO extends LoginFormDTO {

    private String verifyPassword;

    @NotBlank(message = "Name required")
    @NotNull
    @Size(max = 25)
    private String firstName;

    @NotBlank(message = "Last name required")
    @NotNull
    @Size(max = 50)
    private String lastName;

    @Email
    @NotBlank(message = "Email required")
    private String email;

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
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
}
