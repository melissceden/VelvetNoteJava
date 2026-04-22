package com.example.velvet_note.dao;

import com.example.velvet_note.modelo.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // Obtiene todos los productos activos (con estado 1)
    public List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.producto_id, p.titulo, p.artista, p.genero, p.formato, " +
                "p.precio_venta, p.stock, COALESCE(SUM(dv.cantidad), 0) AS total_ventas " +
                "FROM PRODUCTO p " +
                "LEFT JOIN DETALLE_VENTA dv ON p.producto_id = dv.producto_id " +
                "WHERE p.estado = 1 " +
                "GROUP BY p.producto_id, p.titulo, p.artista, p.genero, p.formato, p.precio_venta, p.stock";

        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                productos.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener productos: " + e.getMessage());
        }
        return productos;
    }

    // Obtiene los productos ordenados por mayor stock disponible
    public List<Producto> obtenerPorMayorStock() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.producto_id, p.titulo, p.artista, p.genero, p.formato, " +
                "p.precio_venta, p.stock, COALESCE(SUM(dv.cantidad), 0) AS total_ventas " +
                "FROM PRODUCTO p " +
                "LEFT JOIN DETALLE_VENTA dv ON p.producto_id = dv.producto_id " +
                "WHERE p.estado = 1 " +
                "GROUP BY p.producto_id, p.titulo, p.artista, p.genero, p.formato, p.precio_venta, p.stock " +
                "ORDER BY p.stock DESC";

        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { productos.add(mapear(rs)); }
        } catch (SQLException e) { System.out.println("Error en mayor stock: " + e.getMessage()); }
        return productos;
    }

    // Obtiene los productos ordenados por menor stock disponible (crítico)
    public List<Producto> obtenerPorMenorStock() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.producto_id, p.titulo, p.artista, p.genero, p.formato, " +
                "p.precio_venta, p.stock, COALESCE(SUM(dv.cantidad), 0) AS total_ventas " +
                "FROM PRODUCTO p " +
                "LEFT JOIN DETALLE_VENTA dv ON p.producto_id = dv.producto_id " +
                "WHERE p.estado = 1 " +
                "GROUP BY p.producto_id, p.titulo, p.artista, p.genero, p.formato, p.precio_venta, p.stock " +
                "ORDER BY p.stock ASC";

        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { productos.add(mapear(rs)); }
        } catch (SQLException e) { System.out.println("Error en menor stock: " + e.getMessage()); }
        return productos;
    }

    // Obtiene los productos más vendidos sumando las cantidades de los detalles
    public List<Producto> obtenerPorMasVendidos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.producto_id, p.titulo, p.artista, p.genero, p.formato, " +
                "p.precio_venta, p.stock, COALESCE(SUM(dv.cantidad), 0) AS total_ventas " +
                "FROM PRODUCTO p " +
                "LEFT JOIN DETALLE_VENTA dv ON p.producto_id = dv.producto_id " +
                "WHERE p.estado = 1 " +
                "GROUP BY p.producto_id, p.titulo, p.artista, p.genero, p.formato, p.precio_venta, p.stock " +
                "ORDER BY total_ventas DESC";

        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { productos.add(mapear(rs)); }
        } catch (SQLException e) { System.out.println("Error en más vendidos: " + e.getMessage()); }
        return productos;
    }

    // Obtiene los productos menos vendidos (incluyendo los que tienen cero ventas)
    public List<Producto> obtenerPorMenosVendidos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.producto_id, p.titulo, p.artista, p.genero, p.formato, " +
                "p.precio_venta, p.stock, COALESCE(SUM(dv.cantidad), 0) AS total_ventas " +
                "FROM PRODUCTO p " +
                "LEFT JOIN DETALLE_VENTA dv ON p.producto_id = dv.producto_id " +
                "WHERE p.estado = 1 " +
                "GROUP BY p.producto_id, p.titulo, p.artista, p.genero, p.formato, p.precio_venta, p.stock " +
                "ORDER BY total_ventas ASC";

        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { productos.add(mapear(rs)); }
        } catch (SQLException e) { System.out.println("Error en menos vendidos: " + e.getMessage()); }
        return productos;
    }

    // Cambia el estado a 0 para ocultar el producto (eliminación lógica)
    public boolean eliminar(int productoId) {
        String sql = "UPDATE PRODUCTO SET estado = 0 WHERE producto_id = ?";
        // Al poner la conexión y el stmt entre paréntesis, Java los cierra solo
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, productoId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    // Inserta un nuevo producto con sus datos básicos
    public boolean agregar(Producto p) {
        String sql = "INSERT INTO PRODUCTO (titulo, artista, genero, formato, precio_venta, stock) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, p.getTitulo());
            stmt.setString(2, p.getArtista());
            stmt.setString(3, p.getGenero());
            stmt.setString(4, p.getFormato());
            stmt.setBigDecimal(5, p.getPrecioVenta());
            stmt.setInt(6, p.getStock());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { System.out.println("Error al agregar: " + e.getMessage()); }
        return false;
    }

    // Actualiza la cantidad de stock disponible
    public void actualizarStock(int productoId, int nuevoStock) {
        String sql = "UPDATE PRODUCTO SET stock = ? WHERE producto_id = ?";
        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, nuevoStock);
            stmt.setInt(2, productoId);
            stmt.executeUpdate();
        } catch (SQLException e) { System.out.println("Error al actualizar stock: " + e.getMessage()); }
    }

    // Convierte una fila de la base de datos en un objeto Producto
    private Producto mapear(ResultSet rs) throws SQLException {
        int id = rs.getInt("producto_id");
        String titulo = rs.getString("titulo");
        String artista = rs.getString("artista");
        String genero = rs.getString("genero");
        String formato = rs.getString("formato");
        java.math.BigDecimal precio = rs.getBigDecimal("precio_venta");
        int stock = rs.getInt("stock");
        int ventas = rs.getInt("total_ventas");

        return new Producto(id, titulo, artista, genero, formato, precio, stock, ventas);
    }

    // Obtiene solo los productos eliminados (estado 0)
    public List<Producto> obtenerEliminados() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.producto_id, p.titulo, p.artista, p.genero, p.formato, " +
                "p.precio_venta, p.stock, 0 AS total_ventas FROM PRODUCTO p WHERE p.estado = 0";
        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { productos.add(mapear(rs)); }
        } catch (SQLException e) { System.out.println("Error en papelera: " + e.getMessage()); }
        return productos;
    }

    // Cambia el estado de un producto de vuelta a 1
    public boolean restaurar(int productoId) {
        String sql = "UPDATE PRODUCTO SET estado = 1 WHERE producto_id = ?";
        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, productoId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { System.out.println("Error al restaurar: " + e.getMessage()); }
        return false;
    }
}