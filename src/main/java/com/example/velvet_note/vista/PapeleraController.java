package com.example.velvet_note.vista;

import com.example.velvet_note.dao.ProductoDAO;
import com.example.velvet_note.modelo.Producto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PapeleraController {

    @FXML private TableView<Producto> tablaPapelera;
    @FXML private TableColumn<Producto, Integer> colId, colVendidos;
    @FXML private TableColumn<Producto, String> colTitulo, colArtista;

    private ProductoDAO productoDAO = new ProductoDAO();

    // Se ejecuta al abrir la ventana
    @FXML
    public void initialize() {
        // Configura las columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("productoId"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colArtista.setCellValueFactory(new PropertyValueFactory<>("artista"));
        colVendidos.setCellValueFactory(new PropertyValueFactory<>("totalVentas"));

        // Carga los productos con estado 0
        cargarDatos();
    }

    // Refresca la lista de la papelera
    private void cargarDatos() {
        tablaPapelera.setItems(FXCollections.observableArrayList(productoDAO.obtenerEliminados()));
    }

    // Cambia el estado del producto de 0 a 1
    @FXML
    public void handleRestaurar() {
        Producto seleccionado = tablaPapelera.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            // Llama al DAO para restaurar
            productoDAO.restaurar(seleccionado.getProductoId());
            // Recarga la tabla para que el producto ya no aparezca aquí
            cargarDatos();
            System.out.println("Producto restaurado: " + seleccionado.getTitulo());
        }
    }

    // Cierra la ventana actual
    @FXML
    public void handleCerrar() {
        Stage stage = (Stage) tablaPapelera.getScene().getWindow();
        stage.close();
    }
}