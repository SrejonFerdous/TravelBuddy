package com.example.travelbuddy;

public class orgUser {
    private String name;
    private String userName;
    private String emailOrPhone;
    private String password;

    public String getName() {
        return name;
    }

    public orgUser(String name, String userName, String emailOrPhone, String password) {
        this.name = name;
        this.userName = userName;
        this.emailOrPhone = emailOrPhone;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmailOrPhone() {
        return emailOrPhone;
    }

    public String getPassword() {
        return password;
    }
}
