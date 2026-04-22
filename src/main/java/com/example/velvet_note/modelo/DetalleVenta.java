package com.example.velvet_note.modelo;

import java.math.BigDecimal;

public class DetalleVenta {

    // Atributos de la tabla Cliente
    private int          ventaId;
    private int          productoId;
    private String       productoTitulo;
    private int          cantidad;
    private BigDecimal   precioUnitario;

    // Constructor vacio
    public DetalleVenta() {}

    // Constructor para inicializar todos los atributos
    public DetalleVenta(int ventaId, int productoId, String productoTitulo,
                        int cantidad, BigDecimal precioUnitario) {
        this.ventaId             = ventaId;
        this.productoId          = productoId;
        this.productoTitulo      = productoTitulo;
        this.cantidad            = cantidad;
        this.precioUnitario      = precioUnitario;
    }

    // GETTERS — permiten leer los atributos desde otros archivos
    public int          getVentaId()         { return ventaId;         }
    public int          getProductoId()      { return productoId;      }
    public String       getProductoTitulo()  { return productoTitulo;  }
    public int          getCantidad()        { return cantidad;        }
    public BigDecimal   getPrecioUnitario()  { return precioUnitario;  }

    // SETTERS — permiten modificar los atributos desde otros archivos
    public void setVentaId(int ventaId)                      { this.ventaId         = ventaId;        }
    public void setProductoId(int productoId)                { this.productoId      = productoId;     }
    public void setProductoTitulo(String productoTitulo)     { this.productoTitulo  = productoTitulo; }
    public void setCantidad(int cantidad)                    { this.cantidad        = cantidad;       }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario  = precioUnitario; }

    // Calcula el subtotal de este renglón: cantidad x precio_unitario.
    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}