package com.example.velvet_note.vista;

import com.example.velvet_note.dao.ProductoDAO;        // Acceso a datos de productos
import com.example.velvet_note.modelo.Producto;        // Modelo Producto
import javafx.collections.FXCollections;               // Lista observable para JavaFX
import javafx.fxml.FXML;                               // Vincula con FXML
import javafx.scene.control.TableColumn;               // Columnas de tabla
import javafx.scene.control.TableView;                 // Tabla de datos
import javafx.scene.control.cell.PropertyValueFactory; // Mapea atributos a columnas
import javafx.stage.Stage;                             // Ventana actual

public class PapeleraController {

    @FXML private TableView<Producto> tablaPapelera;              // Tabla principal
    @FXML private TableColumn<Producto, Integer> colId, colVendidos; // Columnas numéricas
    @FXML private TableColumn<Producto, String> colTitulo, colArtista; // Columnas texto

    private ProductoDAO productoDAO = new ProductoDAO(); // DAO para operaciones

    @FXML
    public void initialize() { // Se ejecuta al abrir la vista

        colId.setCellValueFactory(new PropertyValueFactory<>("productoId"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colArtista.setCellValueFactory(new PropertyValueFactory<>("artista"));
        colVendidos.setCellValueFactory(new PropertyValueFactory<>("totalVentas"));

        cargarDatos(); // Carga datos iniciales
    }

    private void cargarDatos() { // Carga productos eliminados
        tablaPapelera.setItems(
                FXCollections.observableArrayList(productoDAO.obtenerEliminados()) // Lista a tabla
        );
    }

    @FXML
    public void handleRestaurar() {

        Producto seleccionado = tablaPapelera.getSelectionModel().getSelectedItem(); // Selección actual

        if (seleccionado != null) { // Verifica selección válida
            productoDAO.restaurar(seleccionado.getProductoId()); // Cambia estado a activo
            cargarDatos(); // Refresca tabla
            System.out.println("Producto restaurado: " + seleccionado.getTitulo()); // Log
        }
    }

    @FXML
    public void handleCerrar() {
        Stage stage = (Stage) tablaPapelera.getScene().getWindow(); // Obtiene ventana
        stage.close(); // Cierra ventana
    }
}