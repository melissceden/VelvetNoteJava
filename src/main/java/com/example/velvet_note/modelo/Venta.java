package com.example.velvet_note.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modelo que representa una venta en la base de datos.
 * Mapea la tabla VENTA.
 */
public class Venta {

    private int ventaId;
    private LocalDateTime fecha;
    private int clienteId;
    private String clienteNombre;
    private String metodoPago;
    private BigDecimal total;

    // Constructor vacío (necesario para JavaFX)
    public Venta() {}

    // Constructor completo
    public Venta(int ventaId, LocalDateTime fecha, int clienteId,
                 String clienteNombre, String metodoPago, BigDecimal total) {
        this.ventaId       = ventaId;
        this.fecha         = fecha;
        this.clienteId     = clienteId;
        this.clienteNombre = clienteNombre;
        this.metodoPago    = metodoPago;
        this.total         = total;
    }

    // getters — permiten leer los atributos desde otros archivos
    public int              getVentaId()        { return ventaId;       }
    public LocalDateTime    getFecha()          { return fecha;         }
    public int              getClienteId()      { return clienteId;     }
    public String           getClienteNombre()  { return clienteNombre; }
    public String           getMetodoPago()     { return metodoPago;    }
    public BigDecimal       getTotal()          { return total;         }

    // setters — permiten modificar los atributos desde otros archivos
    public void setVentaId(int ventaId)                    { this.ventaId        = ventaId;       }
    public void setFecha(LocalDateTime fecha)              { this.fecha          = fecha;         }
    public void setClienteId(int clienteId)                { this.clienteId      = clienteId;     }
    public void setClienteNombre(String clienteNombre)     { this.clienteNombre  = clienteNombre; }
    public void setMetodoPago(String metodoPago)           { this.metodoPago     = metodoPago;    }
    public void setTotal(BigDecimal total)                 { this.total          = total;         }


    @Override
    public String toString() {
        return "Venta{id=" + ventaId + ", cliente=" + clienteNombre
                + ", total=" + total + "}";
    }
}