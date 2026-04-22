package com.example.velvet_note.dao;

import java.sql.*;
import java.math.BigDecimal;

public class EstadisticaDAO {

    // Obtiene la suma total de dinero vendido en el mes actual
    public BigDecimal getVentasDelMes() {
        BigDecimal total = BigDecimal.ZERO;
        String sql = "SELECT COALESCE(SUM(total), 0) FROM VENTA " +
                     "WHERE MONTH(fecha) = MONTH(CURRENT_DATE()) " +
                     "AND YEAR(fecha) = YEAR(CURRENT_DATE())";

        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) total = rs.getBigDecimal(1);
        } catch (SQLException e) {
            System.out.println("Error en ventas del mes: " + e.getMessage());
        }
        return total;
    }

    // Obtiene el nombre del producto que tiene más ventas registradas
    public String getProductoMasVendido() {
        String resultado = "Sin datos";
        String sql = "SELECT titulo FROM PRODUCTO " +
                     "WHERE estado = 1 " +
                     "ORDER BY total_ventas " +
                     "DESC LIMIT 1";

        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) resultado = rs.getString("titulo");
        } catch (SQLException e) {
            System.out.println("Error en producto más vendido: " + e.getMessage());
        }
        return resultado;
    }

    // Obtiene el nombre del producto con menos éxito en ventas
    public String getProductoMenosVendido() {
        String resultado = "Sin datos";
        String sql = "SELECT titulo " +
                "FROM PRODUCTO WHERE estado = 1 " +
                "ORDER BY total_ventas ASC LIMIT 1";

        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) resultado = rs.getString("titulo");
        } catch (SQLException e) {
            System.out.println("Error en producto menos vendido: " + e.getMessage());
        }
        return resultado;
    }

    // --- SECCIÓN DE INVENTARIO ---

    // Obtiene la cantidad total de artículos físicos sumando todos los stocks
    public int getTotalProductosFisicos() {
        int total = 0;
        String sql = "SELECT SUM(stock) FROM PRODUCTO WHERE estado = 1";

        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) total = rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error en total físico: " + e.getMessage());
        }
        return total;
    }

    // Calcula el valor monetario de todo el inventario actual
    public BigDecimal getValorTotalInventario() {
        BigDecimal total = BigDecimal.ZERO;
        String sql = "SELECT SUM(stock * precio_venta) " +
                     "FROM PRODUCTO WHERE estado = 1";

        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) total = rs.getBigDecimal(1);
        } catch (SQLException e) {
            System.out.println("Error en valor inventario: " + e.getMessage());
        }
        return total;
    }

    // Obtiene el conteo de productos cuyo stock llegó a cero
    public int getProductosAgotados() {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM PRODUCTO " +
                     "WHERE stock = 0 AND estado = 1";

        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) total = rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error en productos agotados: " + e.getMessage());
        }
        return total;
    }

    // Obtiene el conteo de productos que tienen menos de 5 unidades
    public int getProductosPocosStock() {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM PRODUCTO " +
                     "WHERE stock < 5 AND stock > 0" +
                     "AND estado = 1";

        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) total = rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error en poco stock: " + e.getMessage());
        }
        return total;
    }

    // --- SECCIÓN DE CLIENTES ---

    // Obtiene el número de clientes que se registraron este mes
    public int getClientesNuevosMes() {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM CLIENTE " +
                     "WHERE MONTH(fecha_registro) = MONTH(CURRENT_DATE()) " +
                     "AND YEAR(fecha_registro) = YEAR(CURRENT_DATE())";

        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) total = rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error en clientes nuevos: " + e.getMessage());
        }
        return total;
    }

    // Obtiene el nombre del cliente que ha gastado más dinero
    public String getTopCliente() {
        String resultado = "Sin datos";
        String sql = "SELECT c.nombre, SUM(v.total) as invertido FROM CLIENTE c " +
                "JOIN VENTA v ON c.cliente_id = v.cliente_id " +
                "GROUP BY c.cliente_id ORDER BY invertido DESC LIMIT 1";

        try {
            Connection con = ConexionDB.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                resultado = rs.getString("nombre") + " (₡" + rs.getBigDecimal("invertido") + ")";
            }
        } catch (SQLException e) {
            System.out.println("Error en top cliente: " + e.getMessage());
        }
        return resultado;
    }
}