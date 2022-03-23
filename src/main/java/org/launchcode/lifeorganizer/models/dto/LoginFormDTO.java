package org.launchcode.lifeorganizer.models.dto;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginFormDTO {
    @Size(min = 6, max = 20, message = "Username must be between 6 and 20 characters")
    private String userName;

    @Size(min = 6,message = "Password must be at least 6 characters long")
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
