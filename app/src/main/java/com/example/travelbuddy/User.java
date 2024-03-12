package com.example.travelbuddy;

public class User {
    private String name;
    private String userName;
    private String emailOrPhone;
    private String password;
    private String photo;

    public String getEmailOrPhone() {
        return emailOrPhone;
    }

    public String getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public User(){}

    public User(String name, String userName, String emailorphone, String password, String photo) {
        this.name = name;
        this.userName = userName;
        this.emailOrPhone = emailorphone;
        this.password = password;
        this.photo=photo;
    }


}
