package org.launchcode.lifeorganizer.models.dto;

import javax.validation.constraints.NotNull;

public class OptionFormDTO extends SignupFormDTO{

    private String newPassword;
    private String currentPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
}
