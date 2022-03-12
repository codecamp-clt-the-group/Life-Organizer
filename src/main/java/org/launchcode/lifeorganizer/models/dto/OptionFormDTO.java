package org.launchcode.lifeorganizer.models.dto;

import org.launchcode.lifeorganizer.models.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OptionFormDTO {
    @NotNull
    @NotBlank
    @Size(min = 6)
    private String newPassword;

    private String verifyPass;

    private User user;
    private String pwdHash;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getVerifyPass() {
        return verifyPass;
    }

    public void setVerifyPass(String verifyPass) {
        this.verifyPass = verifyPass;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPwdHash() {
        return pwdHash;
    }

    public void setPwdHash(String pwdHash) {
        this.pwdHash = pwdHash;
    }
}
