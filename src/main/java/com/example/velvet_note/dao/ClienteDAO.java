package com.example.velvet_note.dao;

import com.example.velvet_note.modelo.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // Obtiene solo los clientes activos (estado = 1)
    public List<Cliente> obtenerTodos() {
        List<Cliente> lista = new ArrayList<>();
        // Filtramos por estado para no mostrar los "eliminados"
        String sql =
                "SELECT * FROM CLIENTE " +
                "WHERE estado = 1 " +
                "ORDER BY cliente_id DESC";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setClienteId(rs.getInt("cliente_id"));
                c.setCedula(rs.getString("cedula"));
                c.setNombre(rs.getString("nombre"));
                c.setApellido(rs.getString("apellido"));
                c.setTelefono(rs.getString("telefono"));
                c.setEmail(rs.getString("email"));

                Date fecha = rs.getDate("fecha_registro");
                if (fecha != null) c.setFechaRegistro(fecha.toLocalDate());

                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        }
        return lista;
    }

    // Registra un nuevo cliente en la base de datos
    public boolean insertar(Cliente c) {
        String sql =
                "INSERT INTO CLIENTE (cedula, nombre, apellido, telefono, email) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getCedula());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getApellido());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getEmail());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar cliente: " + e.getMessage());
            return false;
        }
    }

    // Desactiva al cliente cambiando su estado a 0
    public boolean eliminar(int id) {
        String sql =
                "UPDATE CLIENTE " +
                "SET estado = 0 " +
                "WHERE cliente_id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al desactivar cliente: " + e.getMessage());
            return false;
        }
    }
    // Obtiene clientes inactivos (papelera)
    public List<Cliente> obtenerEliminados() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM CLIENTE WHERE estado = 0 ORDER BY cliente_id DESC";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setClienteId(rs.getInt("cliente_id"));
                c.setCedula(rs.getString("cedula"));
                c.setNombre(rs.getString("nombre"));
                c.setApellido(rs.getString("apellido"));
                c.setTelefono(rs.getString("telefono"));
                c.setEmail(rs.getString("email"));
                Date fecha = rs.getDate("fecha_registro");
                if (fecha != null) c.setFechaRegistro(fecha.toLocalDate());
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar papelera: " + e.getMessage());
        }
        return lista;
    }

    // Reactiva un cliente desde la papelera
    public boolean restaurar(int id) {
        String sql = "UPDATE CLIENTE SET estado = 1 WHERE cliente_id = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al restaurar cliente: " + e.getMessage());
            return false;
        }
    }



}