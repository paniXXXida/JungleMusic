package com.example.jungle_music.model;

import java.io.Serializable;

public class Customer implements Serializable {
    private final String name;
    private final String surname;
    private final String phoneNumber;
    private final String email;

    public Customer(String name, String surname, String phoneNumber, String email) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getName() {
        return name + " " + surname;
    }




}
