package com.example.velvet_note.vista;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.example.velvet_note.dao.ClienteDAO;
import com.example.velvet_note.modelo.Cliente;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClienteController {

    private final ClienteDAO dao = new ClienteDAO();

    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, Integer> colId;
    @FXML private TableColumn<Cliente, String> colCedula;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colApellido;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colEmail;

    @FXML
    public void initialize() {
        ejecutarSeguro(() -> {
            System.out.println("Cargando vista de clientes...");

            // Configuración simplificada de columnas
            colId.setCellValueFactory(new PropertyValueFactory<>("clienteId"));
            colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
            colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

            cargar();
        }, "Error al inicializar la vista");
    }

    private void cargar() {
        ejecutarSeguro(() -> {
            tablaClientes.setItems(
                    FXCollections.observableArrayList(dao.obtenerTodos())
            );
        }, "Error al cargar clientes");
    }

    @FXML
    private void eliminarCliente() {
        ejecutarSeguro(() -> {
            Cliente cliente = tablaClientes.getSelectionModel().getSelectedItem();

            if (cliente == null) {
                mostrarAlerta("Seleccione un cliente para eliminar.");
                return;
            }

            dao.eliminar(cliente.getClienteId());
            System.out.println("Cliente eliminado correctamente.");
            cargar();

        }, "Error al eliminar cliente");
    }

    @FXML
    private void agregarCliente() {
        ejecutarSeguro(() -> {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/AgregarCliente.fxml")
            );
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Agregar Cliente");
            stage.setScene(new Scene(root));
            stage.show();

        }, "Error al abrir ventana de agregar cliente");
    }

    //  Metodo reutilizable para manejo de errores
    private void ejecutarSeguro(Runnable accion, String mensajeError) {
        try {
            accion.run();
        } catch (Exception e) {
            System.out.println(mensajeError + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 🔹 Alerta básica para el usuario
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(mensaje);
        alert.show();
    }
}