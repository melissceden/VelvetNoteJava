package com.example.velvet_note.controladores;

import com.example.velvet_note.dao.ClienteDAO;       // DAO clientes
import com.example.velvet_note.modelo.Cliente;       // Modelo cliente

import javafx.collections.FXCollections;             // Listas observables
import javafx.fxml.FXML;                             // Vinculación FXML
import javafx.fxml.FXMLLoader;                       // Cargar FXML
import javafx.scene.Parent;                          // Nodo raíz
import javafx.scene.Scene;                           // Escena
import javafx.scene.control.*;                       // Controles UI
import javafx.scene.control.cell.PropertyValueFactory; // Mapear columnas
import javafx.stage.Stage;                           // Ventana

import java.io.IOException;                          // Excepción IO

public class ClienteController {

    private final ClienteDAO dao = new ClienteDAO(); // Acceso BD

    @FXML private TableView<Cliente> tablaClientes;  // Tabla clientes
    @FXML private TableColumn<Cliente, Integer> colId;       // ID
    @FXML private TableColumn<Cliente, String> colCedula;    // Cédula
    @FXML private TableColumn<Cliente, String> colNombre;    // Nombre
    @FXML private TableColumn<Cliente, String> colApellido;  // Apellido
    @FXML private TableColumn<Cliente, String> colTelefono;  // Teléfono
    @FXML private TableColumn<Cliente, String> colEmail;     // Email

    @FXML
    public void initialize() {

        System.out.println("Cargando vista de clientes..."); // Debug

        // Configurar columnas (conecta con getters del modelo)
        colId.setCellValueFactory(new PropertyValueFactory<>("clienteId"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        cargar(); // Cargar datos iniciales
    }

    private void cargar() {
        try {
            // Obtener clientes y convertir a lista observable
            tablaClientes.setItems(
                    FXCollections.observableArrayList(dao.obtenerTodos())
            );
        } catch (Exception e) {
            System.out.println("Error al cargar clientes: " + e.getMessage()); // Log
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarCliente() {

        Cliente cliente = tablaClientes.getSelectionModel().getSelectedItem(); // Selección

        if (cliente == null) { // Validar selección
            mostrarAlerta("Seleccione un cliente para eliminar.");
            return;
        }

        try {
            dao.eliminar(cliente.getClienteId()); // Eliminar en BD
            System.out.println("Cliente eliminado correctamente."); // Log
            cargar(); // Refrescar tabla

        } catch (Exception e) {
            System.out.println("Error al eliminar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void agregarCliente() {

        try {

            FXMLLoader loader = new FXMLLoader( // Crear loader con la vista FXML
                    getClass().getResource("/fxml/AgregarCliente.fxml")
            );

            Parent root = loader.load(); // Cargar vista

            Stage stage = new Stage(); // Nueva ventana
            stage.setTitle("Agregar Cliente"); // Título
            stage.setScene(new Scene(root)); // Escena
            stage.show(); // Mostrar

        } catch (IOException e) {
            System.out.println("Error al abrir ventana: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePapelera() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/PapeleraCliente.fxml")
            );
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Papelera de Clientes");
            stage.setScene(new Scene(root));
            stage.show();
            // Al cerrar la papelera, refrescamos la tabla
            stage.setOnHidden(e -> cargar());
        } catch (IOException e) {
            System.out.println("Error al abrir papelera: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING); // Crear alerta
        alert.setContentText(mensaje);
        alert.show(); // Mostrar
    }
}