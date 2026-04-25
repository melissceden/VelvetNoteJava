package com.example.velvet_note.modelo;

import java.math.BigDecimal; // Libreria para manejar decimales

public class DetalleVenta {

    // Atributos de la tabla Cliente, se encapsulan (private) para que no se puedan modificar desde afuer
    private int         ventaId;
    private int         productoId;      // Private "encapsula" las variables
    private String      productoTitulo;  // para que no se puedan modificar
    private int         cantidad;        // afuera
    private BigDecimal  precioUnitario;

    public DetalleVenta() {} // Constructor vacio para poder llenar con los setters luego

    // Constructor para inicializar todos los atributos, con toda la info de golpe
    public DetalleVenta(int ventaId, int productoId, String productoTitulo,
                        int cantidad, BigDecimal precioUnitario) {
        this.ventaId           = ventaId;
        this.productoId        = productoId;
        this.productoTitulo    = productoTitulo;
        this.cantidad          = cantidad;
        this.precioUnitario    = precioUnitario;
    }

    // Getters, se utiliza para obtener un valor
    public int         getVentaId()         { return ventaId;         }
    public int         getProductoId()      { return productoId;      }
    public String      getProductoTitulo()  { return productoTitulo;  }
    public int         getCantidad()        { return cantidad;        }
    public BigDecimal  getPrecioUnitario()  { return precioUnitario;  }

    // Setters, se utiliza para modificar un valor
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