package com.bean;

/**
 * Created by kanaiyalalt on 11/12/2014.
 */
public class ChangePassword {

    private String userId;
    private String exisPassword;
    private String newPassword;
    private String retypeNewPassword;

    public ChangePassword(String userId, String exisPassword, String newPassword, String retypeNewPassword) {
        this.userId = userId;
        this.exisPassword = exisPassword;
        this.newPassword = newPassword;
        this.retypeNewPassword = retypeNewPassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExisPassword() {
        return exisPassword;
    }

    public void setExisPassword(String exisPassword) {
        this.exisPassword = exisPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRetypeNewPassword() {
        return retypeNewPassword;
    }

    public void setRetypeNewPassword(String retypeNewPassword) {
        this.retypeNewPassword = retypeNewPassword;
    }
}
