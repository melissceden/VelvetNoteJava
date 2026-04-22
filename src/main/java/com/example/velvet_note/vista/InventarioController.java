package com.example.velvet_note.vista;

// importaciones necesarias
import com.example.velvet_note.dao.ProductoDAO;
import com.example.velvet_note.modelo.Producto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

// controlador de la vista de inventario
public class InventarioController {

    // tabla y columnas
    @FXML private TableView<Producto> tablaInventario;
    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String>  colTitulo;
    @FXML private TableColumn<Producto, String>  colArtista;
    @FXML private TableColumn<Producto, String>  colGenero;
    @FXML private TableColumn<Producto, String>  colFormato;
    @FXML private TableColumn<Producto, Number>  colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;
    @FXML private TableColumn<Producto, Integer> colVendidos;

    // filtro
    @FXML private ComboBox<String> comboFiltro;

    // dao para acceder a la base de datos
    private ProductoDAO productoDAO = new ProductoDAO();

    // se ejecuta automaticamente al cargar la vista
    @FXML
    public void initialize() {

        try {
            // configurar columnas
            colId.setCellValueFactory(new PropertyValueFactory<>("productoId"));
            colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
            colArtista.setCellValueFactory(new PropertyValueFactory<>("artista"));
            colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
            colFormato.setCellValueFactory(new PropertyValueFactory<>("formato"));
            colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));
            colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
            colVendidos.setCellValueFactory(new PropertyValueFactory<>("totalVentas"));

            // configurar filtro
            comboFiltro.setItems(FXCollections.observableArrayList(
                    "Todos", "Mayor stock", "Menor stock", "Mas vendidos", "Menos vendidos"
            ));
            comboFiltro.getSelectionModel().selectFirst();

            // cargar datos iniciales
            cargarProductos(productoDAO.obtenerTodos());

        } catch (Exception e) {
            System.out.println("Error al inicializar inventario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // carga productos en la tabla
    private void cargarProductos(List<Producto> lista) {

        try {
            ObservableList<Producto> datos = FXCollections.observableArrayList(lista);
            tablaInventario.setItems(datos);

        } catch (Exception e) {
            System.out.println("Error al cargar productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // se ejecuta al cambiar el filtro
    @FXML
    public void handleFiltro() {

        try {
            String seleccion = comboFiltro.getValue();

            if (seleccion == null) return;

            if (seleccion.equals("Mayor stock")) {
                cargarProductos(productoDAO.obtenerPorMayorStock());

            } else if (seleccion.equals("Menor stock")) {
                cargarProductos(productoDAO.obtenerPorMenorStock());

            } else if (seleccion.equals("Mas vendidos")) {
                cargarProductos(productoDAO.obtenerPorMasVendidos());

            } else if (seleccion.equals("Menos vendidos")) {
                cargarProductos(productoDAO.obtenerPorMenosVendidos());

            } else {
                cargarProductos(productoDAO.obtenerTodos());
            }

        } catch (Exception e) {
            System.out.println("Error al aplicar filtro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // eliminar producto
    @FXML
    public void handleEliminar() {

        try {
            Producto seleccionado = tablaInventario.getSelectionModel().getSelectedItem();

            // validar seleccion en caso de que no haya seleccionado ningun
            if (seleccionado == null) {
                System.out.println("No hay producto seleccionado.");

                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Aviso");
                alerta.setHeaderText(null);
                alerta.setContentText("Selecciona un producto primero.");
                alerta.showAndWait();
                return;
            }

            // abrir ventana de confirmacion
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/EliminarProducto.fxml")
            );

            Scene scene = new Scene(loader.load());

            EliminarProductoController controller = loader.getController();
            controller.setProducto(seleccionado);

            Stage ventana = new Stage();
            ventana.setTitle("Eliminar Producto");
            ventana.setScene(scene);
            ventana.initModality(Modality.APPLICATION_MODAL);

            ventana.showAndWait();

            // recargar tabla
            cargarProductos(productoDAO.obtenerTodos());

        } catch (Exception e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // abrir ventana para agregar producto
    @FXML
    public void handleAgregar() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/AgregarProducto.fxml")
            );

            Scene scene = new Scene(loader.load());

            Stage ventana = new Stage();
            ventana.setTitle("Agregar Producto");
            ventana.setScene(scene);
            ventana.initModality(Modality.APPLICATION_MODAL);

            ventana.showAndWait();

            // recargar tabla
            cargarProductos(productoDAO.obtenerTodos());

        } catch (Exception e) {
            System.out.println("Error al abrir ventana de agregar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // abrir papelera
    @FXML
    public void handlePapelera() {

        try {
            System.out.println("Abriendo papelera...");

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/Papelera.fxml")
            );

            Scene scene = new Scene(loader.load());

            Stage ventana = new Stage();
            ventana.setTitle("Papelera");
            ventana.setScene(scene);
            ventana.initModality(Modality.APPLICATION_MODAL);

            ventana.showAndWait();

            // recargar tabla
            cargarProductos(productoDAO.obtenerTodos());

        } catch (Exception e) {
            System.out.println("Error al abrir papelera: " + e.getMessage());
            e.printStackTrace();
        }
    }
}