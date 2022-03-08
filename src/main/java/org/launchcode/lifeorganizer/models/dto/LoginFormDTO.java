package org.launchcode.lifeorganizer.models.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginFormDTO {
    @NotNull
    @NotBlank
    @Size(min = 6, max = 20)
    private String userName;

    @NotNull
    @NotBlank
    @Size(min = 6)
    private String pwdHash;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwdHash() {
        return pwdHash;
    }

    public void setPwdHash(String pwdHash) {
        this.pwdHash = pwdHash;
    }
}
