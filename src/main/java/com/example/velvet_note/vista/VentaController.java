package com.example.velvet_note.vista;

import com.example.velvet_note.dao.VentaDAO;
import com.example.velvet_note.dao.DetalleVentaDAO;
import com.example.velvet_note.modelo.Venta;
import com.example.velvet_note.modelo.DetalleVenta;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controlador de la pantalla Ventas
 * Aquí solo se visualizan las ventas y se pueden eliminar
 */
public class VentaController implements Initializable {

    // ───────────── TABLA DE VENTAS ─────────────
    @FXML private TableView<Venta> tablaVentas;
    @FXML private TableColumn<Venta, String> colId;
    @FXML private TableColumn<Venta, String> colFecha;
    @FXML private TableColumn<Venta, String> colCliente;
    @FXML private TableColumn<Venta, String> colMetodoPago;
    @FXML private TableColumn<Venta, String> colTotal;

    // ───────────── TABLA DE DETALLES ─────────────
    @FXML private TableView<DetalleVenta> tablaDetalles;
    @FXML private TableColumn<DetalleVenta, String> colProducto;
    @FXML private TableColumn<DetalleVenta, String> colCantidad;
    @FXML private TableColumn<DetalleVenta, String> colPrecioUnit;
    @FXML private TableColumn<DetalleVenta, String> colSubtotal;

    // Texto que cambia según la venta seleccionada
    @FXML private Label lblDetallesTitulo;

    // Botón eliminar
    @FXML private Button btnEliminar;

    // ───────────── DAO (conexión directa a BD) ─────────────
    private VentaDAO ventaDAO = new VentaDAO();
    private DetalleVentaDAO detalleDAO = new DetalleVentaDAO();

    // Formato de fecha
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ───────────── INICIO AUTOMÁTICO ─────────────
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnaVentas();
        configurarColumnaDetalles();
        configurarSeleccion();
        cargarVentas();
    }

    // ───────────── CONFIGURAR TABLA DE VENTAS ─────────────
    private void configurarColumnaVentas() {

        // Mostrar ID
        colId.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getVentaId())));

        // Formatear fecha
        colFecha.setCellValueFactory(data -> {
            var fecha = data.getValue().getFecha();
            String texto = (fecha != null) ? fecha.format(FORMATO_FECHA) : "—";
            return new SimpleStringProperty(texto);
        });

        // Cliente
        colCliente.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getClienteNombre()));

        // Método de pago
        colMetodoPago.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getMetodoPago()));

        // Total
        colTotal.setCellValueFactory(data -> {
            BigDecimal total = data.getValue().getTotal();
            String texto = (total != null) ? String.format("%,.2f", total) : "0.00";
            return new SimpleStringProperty(texto);
        });
    }

    // ───────────── CONFIGURAR TABLA DE DETALLES ─────────────
    private void configurarColumnaDetalles() {

        // Nombre del producto
        colProducto.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getProductoTitulo()));

        // Cantidad
        colCantidad.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getCantidad())));

        // Precio unitario
        colPrecioUnit.setCellValueFactory(data -> {
            BigDecimal p = data.getValue().getPrecioUnitario();
            return new SimpleStringProperty(p != null ? String.format("%,.2f", p) : "0.00");
        });

        // Subtotal
        colSubtotal.setCellValueFactory(data -> {
            BigDecimal s = data.getValue().getSubtotal();
            return new SimpleStringProperty(s != null ? String.format("%,.2f", s) : "0.00");
        });
    }

    // ───────────── CUANDO SE SELECCIONA UNA VENTA ─────────────
    private void configurarSeleccion() {
        tablaVentas.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, anterior, seleccionada) -> {

                    // Si selecciona algo → cargar detalles
                    if (seleccionada != null) {
                        cargarDetalles(seleccionada);
                    } else {
                        // Si no hay selección → limpiar
                        tablaDetalles.getItems().clear();
                        lblDetallesTitulo.setText("Selecciona una venta para ver sus detalles");
                    }
                });
    }

    // ───────────── CARGAR TODAS LAS VENTAS ─────────────
    private void cargarVentas() {
        try {
            // Obtener desde la BD
            List<Venta> ventas = ventaDAO.obtenerTodas();

            // Convertir a lista observable (JavaFX)
            ObservableList<Venta> datos = FXCollections.observableArrayList(ventas);

            // Mostrar en tabla
            tablaVentas.setItems(datos);

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Error", "No se pudieron cargar las ventas:\n" + e.getMessage());
        }
    }

    // ───────────── CARGAR DETALLES DE UNA VENTA ─────────────
    private void cargarDetalles(Venta venta) {
        try {
            // Cambiar título dinámico
            lblDetallesTitulo.setText(
                    "Detalles de Venta #" + venta.getVentaId()
                            + " — " + venta.getClienteNombre()
                            + " | " + venta.getFecha().format(FORMATO_FECHA));

            // Obtener detalles desde DAO
            List<DetalleVenta> detalles = detalleDAO.obtenerPorVenta(venta.getVentaId());

            // Mostrar en tabla
            tablaDetalles.setItems(FXCollections.observableArrayList(detalles));

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Error", "No se pudo cargar el detalle:\n" + e.getMessage());
        }
    }

    // ───────────── ELIMINAR VENTA ─────────────
    @FXML
    private void eliminarVenta() {

        // Obtener la venta seleccionada
        Venta seleccionada = tablaVentas.getSelectionModel().getSelectedItem();

        // Validar que haya selección
        if (seleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Sin selección", "Selecciona una venta primero.");
            return;
        }

        // Confirmación
        Optional<ButtonType> respuesta = new Alert(
                Alert.AlertType.CONFIRMATION,
                "¿Eliminar la venta #" + seleccionada.getVentaId() + "?",
                ButtonType.YES, ButtonType.NO
        ).showAndWait();

        // Si confirma
        if (respuesta.isPresent() && respuesta.get() == ButtonType.YES) {
            try {
                // Eliminar desde la BD
                ventaDAO.eliminar(seleccionada.getVentaId());

                // Limpiar detalles
                tablaDetalles.getItems().clear();
                lblDetallesTitulo.setText("Selecciona una venta para ver sus detalles");

                // Recargar tabla
                cargarVentas();

                mostrarAlerta(Alert.AlertType.INFORMATION,
                        "Éxito", "Venta eliminada correctamente.");

            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR,
                        "Error", "No se pudo eliminar:\n" + e.getMessage());
            }
        }
    }

    // ───────────── ALERTAS ─────────────
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}