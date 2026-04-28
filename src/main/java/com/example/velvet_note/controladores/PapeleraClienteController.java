package com.example.velvet_note.controladores;

import com.example.velvet_note.dao.ClienteDAO;
import com.example.velvet_note.modelo.Cliente;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class PapeleraClienteController {

    private final ClienteDAO dao = new ClienteDAO();

    @FXML private TableView<Cliente>              tablaClientes;
    @FXML private TableColumn<Cliente, Integer>   colId;
    @FXML private TableColumn<Cliente, String>    colCedula;
    @FXML private TableColumn<Cliente, String>    colNombre;
    @FXML private TableColumn<Cliente, String>    colApellido;
    @FXML private TableColumn<Cliente, String>    colTelefono;
    @FXML private TableColumn<Cliente, String>    colEmail;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("clienteId"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido")); // ← esta
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        cargar();
    }

    private void cargar() {
        tablaClientes.setItems(
                FXCollections.observableArrayList(dao.obtenerEliminados())
        );
    }

    @FXML
    private void handleRestaurar() {
        Cliente cliente = tablaClientes.getSelectionModel().getSelectedItem();
        if (cliente == null) {
            mostrarAlerta("Seleccione un cliente para restaurar.");
            return;
        }
        dao.restaurar(cliente.getClienteId());
        cargar();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(mensaje);
        alert.show();
    }


}