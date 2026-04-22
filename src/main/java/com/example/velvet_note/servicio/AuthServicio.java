package com.example.velvet_note.servicio;

import com.example.velvet_note.dao.UsuarioDAO;
import com.example.velvet_note.modelo.Usuario;
import org.apache.commons.codec.digest.DigestUtils; // Importa la librería

public class AuthServicio {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Usuario usuarioActivo = null;

    // Busca al usuario y valida su contraseña
    public boolean login(String username, String password) {
        Usuario usuario = usuarioDAO.buscarPorUsername(username);

        if (usuario == null) {
            return false;
        }

        // Usa la librería para encriptar, convierte la contrasena
        String passEncriptada = DigestUtils.sha256Hex(password);

        // Compara los textos
        if (usuario.getPassword().equals(passEncriptada)) {
            usuarioActivo = usuario;
            return true;
        }

        return false;
    }

    public Usuario getUsuarioActivo() {
        return usuarioActivo;
    }

    public void logout() {
        usuarioActivo = null;
    }
}