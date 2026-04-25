package com.example.velvet_note.servicio;

import com.example.velvet_note.dao.EstadisticaDAO;
import java.math.BigDecimal;

public class EstadisticaServicio {

    private EstadisticaDAO estadisticaDAO = new EstadisticaDAO();

    // MÉTODOS DE VENTAS
    public BigDecimal getVentasDelMes() { return estadisticaDAO.getVentasDelMes(); }
    public String getProductoMasVendido() { return estadisticaDAO.getProductoMasVendido(); }
    public String getProductoMenosVendido() { return estadisticaDAO.getProductoMenosVendido(); }

    // MÉTODOS DE INVENTARIO
    public int getTotalProductosFisicos() { return estadisticaDAO.getTotalProductosFisicos(); }
    public int getProductosPocosStock() { return estadisticaDAO.getProductosPocosStock(); }
    public BigDecimal getValorTotalInventario() { return estadisticaDAO.getValorTotalInventario(); }
    public int getProductosAgotados() { return estadisticaDAO.getProductosAgotados(); }

    // MÉTODOS DE CLIENTES
    public int getClientesNuevosMes() { return estadisticaDAO.getClientesNuevosMes(); }
    public String getTopCliente() { return estadisticaDAO.getTopCliente(); }
}