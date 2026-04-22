package com.example.velvet_note.vista;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AgregarClienteController {

    @FXML private TextField txtCedula;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;

    // se ejecuta cuando el usuario presiona guardar
    @FXML
    private void guardarCliente() {

        try {
            // obtiene los datos escritos en los campos
            String cedula   = txtCedula.getText().trim();
            String nombre   = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String email    = txtEmail.getText().trim();

            // valida que los campos obligatorios no esten vacios
            if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
                System.out.println("Error: faltan campos obligatorios.");
                return;
            }

            // imprime los datos en consola (prueba)
            System.out.println("Guardando cliente...");
            System.out.println("Cédula: " + cedula);
            System.out.println("Nombre: " + nombre + " " + apellido);

            // aqui iria el DAO para guardar en la base de datos
            // ClienteDAO clienteDAO = new ClienteDAO();
            // clienteDAO.agregar(...);

            // cierra la ventana despues de guardar
            cerrarVentana();

        } catch (Exception e) {
            // si ocurre cualquier error lo imprime en consola
            System.out.println("Error al guardar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // metodo reutilizable para cerrar la ventana
    private void cerrarVentana() {
        Stage stage = (Stage) txtCedula.getScene().getWindow();
        stage.close();
    }

    // se ejecuta cuando el usuario presiona cancelar
    @FXML
    private void cancelar() {
        cerrarVentana();
    }
}