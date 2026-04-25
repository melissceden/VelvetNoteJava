package com.example.velvet_note.vista;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AgregarClienteController {

    @FXML private TextField txtCedula;
    @FXML private TextField txtNombre;   // FXML aclara que son elementos del FXML de agregar cliente
    @FXML private TextField txtApellido; // private encapsula las variables
    @FXML private TextField txtTelefono; //
    @FXML private TextField txtEmail;

    // se ejecuta cuando el usuario presiona guardar
    @FXML
    private void guardarCliente() {

        try {
            // obtiene los datos escritos en los campos
            String cedula   = txtCedula.getText().trim(); // .trim elimina los espacios finales e iniciales si hay
            String nombre   = txtNombre.getText().trim(); // .getText() obtiene el texto escrito por el usuario
            String apellido = txtApellido.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String email    = txtEmail.getText().trim();

            // valida que los campos obligatorios no esten vacios
            if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) { // .isEmpty verifica si esta vacoi
                System.out.println("Error: faltan campos obligatorios.");
                return;
            }

            // imprime los datos en consola (prueba)
            System.out.println("Guardando cliente...");
            System.out.println("Cédula: " + cedula);
            System.out.println("Nombre: " + nombre + " " + apellido);

            cerrarVentana(); // Cierra la ventana

        } catch (Exception e) {
            // si ocurre cualquier error lo imprime en consola
            System.out.println("Error al guardar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // metodo reutilizable para cerrar la ventana
    private void cerrarVentana() {
        Stage stage = (Stage) txtCedula.getScene().getWindow();
        stage.close(); // Cierra la ventana
    }

    // se ejecuta cuando el usuario presiona cancelar. se cerrara la ventana
    @FXML
    private void cancelar() {
        cerrarVentana();
    }
}