package com.example.velvet_note.dao;

import com.example.velvet_note.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    // Busca un usuario en la base de datos por su nombre de usuario
    public Usuario buscarPorUsername(String username) {
        String sql = "SELECT * FROM USUARIO WHERE username = ?";

        try {
            // Obtiene la conexión y prepara la consulta
            Connection con = ConexionDB.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql);

            // Asigna el parámetro a la consulta
            stmt.setString(1, username);

            // Ejecuta la búsqueda
            ResultSet rs = stmt.executeQuery();

            // Si encuentra el registro, crea el objeto Usuario con sus datos
            if (rs.next()) {
                int id = rs.getInt("usuario_id");
                String user = rs.getString("username");
                String pass = rs.getString("password");

                return new Usuario(id, user, pass);
            }

        } catch (SQLException e) {
            // Imprime el error en consola si algo falla
            System.out.println("Error al obtener usuario: " + e.getMessage());
        }

        // Devuelve null si no existe el usuario o hubo un error
        return null;
    }
}