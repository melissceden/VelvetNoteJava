package com.example.velvet_note.modelo;

public class Usuario {

    private int usuarioId;
    private String username;
    private String password;

    // Constructor
    public Usuario(int usuarioId, String username, String password) {
        this.usuarioId = usuarioId;
        this.username = username;
        this.password = password;
    }

    // GETTERS -- permiten leer los atributos desde otros archivos
    public int      getUsuarioId()       { return usuarioId; }
    public String   getUsername()        { return username; }
    public String   getPassword()        { return password; }

    // SETTERS -- permiten modificar los atributos desde otros archivos
    public void setUsuarioId(int usuarioId)     { this.usuarioId = usuarioId; }
    public void setUsername(String username)    { this.username  = username;  }
    public void setPassword(String password)    { this.password  = password;  }
}