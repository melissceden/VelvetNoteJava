// COMENTADO con manejo de errores

package com.example.velvet_note.dao;

import com.example.velvet_note.modelo.DetalleVenta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaDAO {

    //  Obtiene todos los detalles asociados a una venta especifica
    public List<DetalleVenta> obtenerPorVenta(int ventaId) {
        List<DetalleVenta> lista = new ArrayList<>();

        String sql = """ 
                SELECT dv.venta_id, dv.producto_id,
                       COALESCE(p.titulo, 'Producto eliminado') AS producto_titulo,
                       dv.cantidad, dv.precio_unitario
                FROM DETALLE_VENTA dv
                LEFT JOIN PRODUCTO p ON dv.producto_id = p.producto_id
                WHERE dv.venta_id = ?
                ORDER BY dv.producto_id
                """;
        // Da que productos se vendieron en la venta numero X
        // Trayendo el nombre del producto si existe
        // junto a su precio, cantidad, y precio
        // los ordena por ID
        // COALESCE: funcion de seguridad, si al buscar un producto da NULL
        // imprimira "Producto eliminado" en su lugar

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ventaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleVenta d = new DetalleVenta();
                    d.setVentaId(rs.getInt("venta_id"));
                    d.setProductoId(rs.getInt("producto_id"));
                    d.setProductoTitulo(rs.getString("producto_titulo"));
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
                    lista.add(d);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener detalle de venta: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
    // Metodo para eliminar todos los detalles de una venta
    public void eliminarPorVenta(int ventaId) {
        String sql = "DELETE FROM DETALLE_VENTA WHERE venta_id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ventaId);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al eliminar detalle de venta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Inserta un nuevo detalle de venta
    public void insertar(DetalleVenta detalle) {

        String sql = "INSERT INTO DETALLE_VENTA (venta_id, producto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, detalle.getVentaId());
            stmt.setInt(2, detalle.getProductoId());
            stmt.setInt(3, detalle.getCantidad());
            stmt.setBigDecimal(4, detalle.getPrecioUnitario());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al ingresar detalle de venta: " + e.getMessage());
            e.printStackTrace();
        }
    }

}