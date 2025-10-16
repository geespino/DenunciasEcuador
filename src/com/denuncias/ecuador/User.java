package com.denuncias.ecuador;

public class User {
    private String username;
    private String password; 

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String pass) {
        return this.password.equals(pass);
    }

    public boolean isAdmin() {
        return this.username.equals("admin") ? true : false;
    }
}
