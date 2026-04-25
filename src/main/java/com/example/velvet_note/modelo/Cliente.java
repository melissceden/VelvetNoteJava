package com.example.velvet_note.modelo;

import java.time.LocalDate;

public class Cliente {

    // Atributos de la tabla Cliente
    private int       clienteId;
    private String    cedula;
    private String    nombre;
    private String    apellido;
    private String    telefono;
    private String    email;
    private LocalDate fechaRegistro;

    // GETTERS — permiten leer los atributos desde otros archivos
    public int        getClienteId()      { return clienteId;      }
    public String     getCedula()         { return cedula;         }
    public String     getNombre()         { return nombre;         }
    public String     getApellido()       { return apellido;       }
    public String     getTelefono()       { return telefono;       }
    public String     getEmail()          { return email;          }
    public LocalDate  getFechaRegistro()  { return fechaRegistro;  }

    // SETTERS — permiten modificar los atributos desde otros archivos
    public void setClienteId(int clienteId)                 { this.clienteId      = clienteId;     }
    public void setCedula(String cedula)                    { this.cedula         = cedula;        }
    public void setNombre(String nombre)                    { this.nombre         = nombre;        }
    public void setApellido(String apellido)                { this.apellido       = apellido;      }
    public void setTelefono(String telefono)                { this.telefono       = telefono;      }
    public void setEmail(String email)                      { this.email          = email;         }
    public void setFechaRegistro(LocalDate fechaRegistro)   { this.fechaRegistro  = fechaRegistro; }
}