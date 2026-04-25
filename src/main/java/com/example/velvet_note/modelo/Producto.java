package com.example.velvet_note.modelo;

import java.math.BigDecimal;

// Clase que representa una fila de la tabla PRODUCTO en la bd
public class Producto {

    // Cada atributo corresponde a una columna de la tabla
    private int         productoId;
    private String      titulo;            // nombre del album
    private String      artista;           // nombre del artista
    private String      genero;            // Rock, Jazz, etc
    private String      formato;           // Vinilo, CD, Casete
    private BigDecimal  precioVenta;       // precio del producto
    private int         stock;             // cantidad disponibles
    private int         totalVentas;       // total vendidos

    // Constructor, para que reciban la informacion de una vez
    public Producto(int productoId, String titulo, String artista, String genero,
                    String formato, BigDecimal precioVenta, int stock, int totalVentas) {

        this.productoId   = productoId;
        this.titulo       = titulo;
        this.artista      = artista;
        this.genero       = genero;
        this.formato      = formato;
        this.precioVenta  = precioVenta;
        this.stock        = stock;
        this.totalVentas  = totalVentas;

    }

    // GETTERS -- permiten leer los atributos desde otros archivos
    public int         getProductoId()    { return productoId;  }
    public String      getTitulo()        { return titulo;      }
    public String      getArtista()       { return artista;     }
    public String      getGenero()        { return genero;      }
    public String      getFormato()       { return formato;     }
    public BigDecimal  getPrecioVenta()   { return precioVenta; }
    public int         getStock()         { return stock;       }
    public int         getTotalVentas()   { return totalVentas; }

    // SETTERS -- permiten modificar los atributos desde otros archivos
    public void setProductoId(int productoId)            { this.productoId    = productoId; }
    public void setTitulo(String titulo)                 { this.titulo        = titulo;     }
    public void setArtista(String artista)               { this.artista       = artista;    }
    public void setGenero(String genero)                 { this.genero        = genero;     }
    public void setFormato(String formato)               { this.formato       = formato;    }
    public void setPrecioVenta(BigDecimal precioVenta)   { this.precioVenta   = precioVenta;}
    public void setStock(int stock)                      { this.stock         = stock;      }
    public void setTotalVentas(int totalVentas)          { this.totalVentas   = totalVentas;}
}