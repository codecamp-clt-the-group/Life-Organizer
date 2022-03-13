package org.launchcode.lifeorganizer.models.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OptionFormDTO extends SignupFormDTO{
    @NotNull
    @NotBlank
    @Size(min = 6)
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
