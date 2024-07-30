package com.example.crudapp;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String nombres;
    private String apellidos;
    private String password;
    private String token;

    // Getters and setters
    public int getId() { return id;
    }

    public void setId(int id) { this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
