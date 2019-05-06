package com.indev.scrapx.data.model;

import org.jsoup.Connection;

public class LoginInformationClass {
    private String loginPage;
    private Connection.Method loginMethod;
    private String userName;
    private String password;
    private String userNameInput;
    private String passwordInput;

    public LoginInformationClass(){}
    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserNameInput(String userNameInput) {
        this.userNameInput = userNameInput;
    }

    public void setPasswordInput(String passwordInput) {
        this.passwordInput = passwordInput;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getUserNameInput() {
        return userNameInput;
    }

    public String getPasswordInput() {
        return passwordInput;
    }

    public Connection.Method getLoginMethod() {
        return loginMethod;
    }

    public void setLoginMethod(Connection.Method loginMethod) {
        this.loginMethod = loginMethod;
    }
}
