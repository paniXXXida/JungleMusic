package com.example.jungle_music.model;


import java.io.Serializable;

public class User implements Serializable {
    private final String username;
    private final String name;
    private final String surname;
    private final String password;
    private final Role role;

    public User(String username, String name, String surname, String password, Role role) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }


    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
