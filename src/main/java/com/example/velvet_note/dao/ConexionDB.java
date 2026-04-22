package com.example.velvet_note.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    // Datos de acceso
    private static String url = "jdbc:mariadb://localhost:3306/velvet_note_db";
    private static String usuario = "velvet";
    private static String clave = "1234";

    // Método para obtener la conexión
    public static Connection getConexion() {
        Connection conexion = null; // Empezamos con la conexión vacía

        try {
            // Intenta conectar
            conexion = DriverManager.getConnection(url, usuario, clave);
            System.out.println("¡Conexión establecida con éxito!");

        } catch (SQLException e) {
            // Por si hay algun problema en la conexion
            System.out.println("No se pudo conectar a la base de datos.");
            System.out.println("El error fue: " + e.getMessage());
        }

        return conexion; // Se devuelve la conexion (o null si falló)
    }
}