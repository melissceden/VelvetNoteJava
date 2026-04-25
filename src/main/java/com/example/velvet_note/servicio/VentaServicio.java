package com.example.velvet_note.servicio;

import com.example.velvet_note.dao.VentaDAO;
import com.example.velvet_note.dao.DetalleVentaDAO;
import com.example.velvet_note.modelo.Venta;
import com.example.velvet_note.modelo.DetalleVenta;

import java.util.ArrayList;  // Librerias necesarias para manejar
import java.util.List;       // listas de datos

public class VentaServicio {

    // Define los objetos DAO para acceder a los datos de ventas y sus detalles
    private VentaDAO ventaDAO = new VentaDAO();
    private DetalleVentaDAO detalleDAO = new DetalleVentaDAO();

    // Obtiene la lista completa de todas las ventas realizadas
    public List<Venta> obtenerTodas() {
        try {
            return ventaDAO.obtenerTodas(); // Llama al DAO para traer los registros de la base de datos
        } catch (Exception e) {
            System.out.println("Error al obtener ventas: " + e.getMessage());
            return new ArrayList<>(); // Devuelve una lista vacia
        }
    }

    // Obtiene los productos específicos vinculados a una venta por su ID
    public List<DetalleVenta> obtenerDetalles(int ventaId) {
        try {
            // Busca en el DAO de detalles usando el ID de la venta principal
            return detalleDAO.obtenerPorVenta(ventaId);
        } catch (Exception e) {
            System.out.println("Error al obtener detalles: " + e.getMessage());
            return new ArrayList<>(); // Devuelve una lista vacia
        }
    }

    // Elimina una venta completa, incluyendo sus detalles asociados
    public void eliminarVenta(int ventaId) {
        try {
            // Elimina primero los detalles
            detalleDAO.eliminarPorVenta(ventaId);
            // Elimina la venta principal una vez que sus "hijos" han sido borrados
            ventaDAO.eliminar(ventaId);

            System.out.println("Venta y detalles eliminados correctamente.");
        } catch (Exception e) {
            System.out.println("Error al eliminar venta: " + e.getMessage());
        }
    }
}