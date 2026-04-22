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

public class ClienteController {

    // dao para acceder a los datos de clientes
    private final ClienteDAO dao = new ClienteDAO();

    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, Integer> colId;
    @FXML private TableColumn<Cliente, String> colCedula;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colApellido;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colEmail;

    // se ejecuta automaticamente al cargar la vista
    @FXML
    public void initialize() {

        try {
            System.out.println("Cargando vista de clientes...");

            // configurar columnas
            colId.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getClienteId()).asObject());
            colCedula.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCedula()));
            colNombre.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNombre()));
            colApellido.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getApellido()));
            colTelefono.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getTelefono()));
            colEmail.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getEmail()));

            // cargar datos en la tabla
            cargar();

        } catch (Exception e) {
            System.out.println("Error al inicializar la vista: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // carga los clientes en la tabla
    private void cargar() {

        try {
            tablaClientes.setItems(FXCollections.observableArrayList(dao.obtenerTodos()));
        } catch (Exception e) {
            System.out.println("Error al cargar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // se ejecuta cuando el usuario presiona eliminar
    @FXML
    private void eliminarCliente() {

        try {
            System.out.println("Botón eliminar cliente presionado");
            // obtiene el cliente seleccionado
            Cliente cliente = tablaClientes.getSelectionModel().getSelectedItem();
            // valida que haya un cliente seleccionado
            if (cliente == null) {
                System.out.println("No hay cliente seleccionado.");
                return;
            }
            // elimina el cliente
            dao.eliminar(cliente.getClienteId());
            System.out.println("Cliente eliminado correctamente.");
            // recarga la tabla
            cargar();
        } catch (Exception e) {
            System.out.println("Error al eliminar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // abre la ventana para agregar un nuevo cliente
    @FXML
    private void agregarCliente() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AgregarCliente.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar Cliente");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            System.out.println("Error al abrir ventana de agregar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}