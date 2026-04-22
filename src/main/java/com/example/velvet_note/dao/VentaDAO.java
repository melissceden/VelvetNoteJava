package com.example.velvet_note.dao;

import com.example.velvet_note.modelo.Venta;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    // Obtiene todas las ventas registradas con el nombre del cliente
    public List<Venta> obtenerTodas() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT v.venta_id, v.fecha, v.cliente_id, " +
                "COALESCE(c.nombre, 'Desconocido') AS cliente_nombre, " +
                "v.metodo_pago, v.total " +
                "FROM VENTA v " +
                "LEFT JOIN CLIENTE c ON v.cliente_id = c.cliente_id " +
                "ORDER BY v.venta_id DESC";

        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapearFila(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener ventas: " + e.getMessage());
        }
        return lista;
    }

    // Obtiene una venta específica buscando por su ID
    public Venta obtenerPorId(int id) {
        String sql = "SELECT v.venta_id, v.fecha, v.cliente_id, " +
                "COALESCE(c.nombre, 'Desconocido') AS cliente_nombre, " +
                "v.metodo_pago, v.total " +
                "FROM VENTA v " +
                "LEFT JOIN CLIENTE c ON v.cliente_id = c.cliente_id " +
                "WHERE v.venta_id = ?";

        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearFila(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar venta: " + e.getMessage());
        }
        return null;
    }

    // Elimina un registro de venta de la base de datos
    public boolean eliminar(int ventaId) {
        String sql = "DELETE FROM VENTA WHERE venta_id = ?";

        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, ventaId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar venta: " + e.getMessage());
        }
        return false;
    }

    // Inserta una nueva venta y devuelve el ID generado por la base de datos
    public int insertar(Venta venta) {
        String sql = "INSERT INTO VENTA (fecha, cliente_id, metodo_pago, total) VALUES (NOW(), ?, ?, 0)";

        try {
            Connection con = ConexionDB.getConexion();
            // Preparamos para recibir el ID generado automáticamente
            PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, venta.getClienteId());
            stmt.setString(2, venta.getMetodoPago());

            stmt.executeUpdate();

            // Recupera el ID asignado a esta nueva venta
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error al insertar venta: " + e.getMessage());
        }
        return -1;
    }

    // Convierte una fila de la base de datos en un objeto Venta
    private Venta mapearFila(ResultSet rs) throws SQLException {
        Venta v = new Venta();

        v.setVentaId(rs.getInt("venta_id"));
        v.setClienteId(rs.getInt("cliente_id"));
        v.setClienteNombre(rs.getString("cliente_nombre"));
        v.setMetodoPago(rs.getString("metodo_pago"));
        v.setTotal(rs.getBigDecimal("total"));

        // Maneja la conversión de fecha de SQL a Java
        Timestamp ts = rs.getTimestamp("fecha");
        if (ts != null) {
            v.setFecha(ts.toLocalDateTime());
        } else {
            v.setFecha(LocalDateTime.now());
        }

        return v;
    }
}