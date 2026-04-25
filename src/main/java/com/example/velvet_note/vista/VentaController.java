package com.example.velvet_note.vista;

import com.example.velvet_note.dao.VentaDAO;        // DAO ventas
import com.example.velvet_note.dao.DetalleVentaDAO; // DAO detalles
import com.example.velvet_note.modelo.Venta;        // Modelo venta
import com.example.velvet_note.modelo.DetalleVenta; // Modelo detalle

import javafx.beans.property.SimpleStringProperty;  // Propiedades tabla
import javafx.collections.FXCollections;            // Listas observables
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;                        // Decimales
import java.net.URL;
import java.time.format.DateTimeFormatter;          // Formato fecha
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class VentaController implements Initializable {

    @FXML private TableView<Venta> tablaVentas;         // Tabla ventas
    @FXML private TableColumn<Venta, String> colId;
    @FXML private TableColumn<Venta, String> colFecha;
    @FXML private TableColumn<Venta, String> colCliente;
    @FXML private TableColumn<Venta, String> colMetodoPago;
    @FXML private TableColumn<Venta, String> colTotal;

    @FXML private TableView<DetalleVenta> tablaDetalles; // Tabla detalles
    @FXML private TableColumn<DetalleVenta, String> colProducto;
    @FXML private TableColumn<DetalleVenta, String> colCantidad;
    @FXML private TableColumn<DetalleVenta, String> colPrecioUnit;
    @FXML private TableColumn<DetalleVenta, String> colSubtotal;

    @FXML private Label lblDetallesTitulo; // Texto dinámico

    private VentaDAO ventaDAO = new VentaDAO();           // Acceso BD ventas
    private DetalleVentaDAO detalleDAO = new DetalleVentaDAO(); // Acceso BD detalles

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); // Formato fecha

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Configurar columnas ventas
        colId.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getVentaId()))); // ID

        colFecha.setCellValueFactory(data -> { // Fecha formateada
            var fecha = data.getValue().getFecha();
            return new SimpleStringProperty(
                    (fecha != null) ? fecha.format(FORMATO_FECHA) : "—"
            );
        });

        colCliente.setCellValueFactory(new PropertyValueFactory<>("clienteNombre")); // Cliente
        colMetodoPago.setCellValueFactory(new PropertyValueFactory<>("metodoPago")); // Método pago

        colTotal.setCellValueFactory(data -> { // Total formateado
            BigDecimal total = data.getValue().getTotal();
            return new SimpleStringProperty(
                    (total != null) ? String.format("%,.2f", total) : "0.00"
            );
        });

        // Configurar columnas detalles
        colProducto.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getProductoTitulo())); // Producto

        colCantidad.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getCantidad()))); // Cantidad

        colPrecioUnit.setCellValueFactory(data -> { // Precio unitario
            BigDecimal p = data.getValue().getPrecioUnitario();
            return new SimpleStringProperty(
                    (p != null) ? String.format("%,.2f", p) : "0.00"
            );
        });

        colSubtotal.setCellValueFactory(data -> { // Subtotal
            BigDecimal s = data.getValue().getSubtotal();
            return new SimpleStringProperty(
                    (s != null) ? String.format("%,.2f", s) : "0.00"
            );
        });

        // Listener cuando selecciona una venta
        tablaVentas.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, seleccionada) -> {

                    if (seleccionada != null) { // Si selecciona

                        try {
                            // Cambiar título
                            lblDetallesTitulo.setText(
                                    "Detalles de Venta #" + seleccionada.getVentaId()
                                            + " — " + seleccionada.getClienteNombre()
                                            + " | " + seleccionada.getFecha().format(FORMATO_FECHA)
                            );

                            // Cargar detalles
                            List<DetalleVenta> detalles =
                                    detalleDAO.obtenerPorVenta(seleccionada.getVentaId());

                            tablaDetalles.setItems(
                                    FXCollections.observableArrayList(detalles)
                            );

                        } catch (Exception e) {
                            mostrarAlerta(Alert.AlertType.ERROR,
                                    "Error", "No se pudo cargar el detalle:\n" + e.getMessage());
                        }

                    } else { // Si no hay selección
                        tablaDetalles.getItems().clear();
                        lblDetallesTitulo.setText("Selecciona una venta para ver sus detalles");
                    }
                });

        cargarVentas(); // Cargar ventas al inicio
    }

    private void cargarVentas() {
        try {
            List<Venta> ventas = ventaDAO.obtenerTodas(); // BD
            tablaVentas.setItems(FXCollections.observableArrayList(ventas)); // Mostrar
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Error", "No se pudieron cargar las ventas:\n" + e.getMessage());
        }
    }

    @FXML
    private void eliminarVenta() {

        Venta seleccionada = tablaVentas.getSelectionModel().getSelectedItem(); // Selección

        if (seleccionada == null) { // Validar
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Sin selección", "Selecciona una venta primero.");
            return;
        }

        Optional<ButtonType> respuesta = new Alert( // Confirmación
                Alert.AlertType.CONFIRMATION,
                "¿Eliminar la venta #" + seleccionada.getVentaId() + "?",
                ButtonType.YES, ButtonType.NO
        ).showAndWait();

        if (respuesta.isPresent() && respuesta.get() == ButtonType.YES) {
            try {
                ventaDAO.eliminar(seleccionada.getVentaId()); // Eliminar BD

                tablaDetalles.getItems().clear(); // Limpiar
                lblDetallesTitulo.setText("Selecciona una venta para ver sus detalles");

                cargarVentas(); // Recargar

                mostrarAlerta(Alert.AlertType.INFORMATION,
                        "Éxito", "Venta eliminada correctamente.");

            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR,
                        "Error", "No se pudo eliminar:\n" + e.getMessage());
            }
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo); // Crear alerta
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait(); // Mostrar
    }
}