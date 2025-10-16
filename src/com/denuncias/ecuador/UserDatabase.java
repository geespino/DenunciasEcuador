package com.denuncias.ecuador;

import java.util.ArrayList;
import java.util.List;

public class UserDatabase {
    private static List<User> users = new ArrayList<>();

    public static boolean registrarUsuario(String username, String password) {
        if (buscarUsuario(username) != null) {
            return false; 
        }
        users.add(new User(username, password));
        return true;
    }

    public static User login(String username, String password) {
        User u = buscarUsuario(username);
        if (u != null && u.checkPassword(password)) {
            return u;
        }
        return null;
    }

    private static User buscarUsuario(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
}

