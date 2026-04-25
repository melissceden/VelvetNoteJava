package com.example.velvet_note.vista;

import com.example.velvet_note.servicio.AuthServicio;
import com.example.velvet_note.servicio.EstadisticaServicio;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;

public class MainController {

    // --- SECCIÓN 1: VENTAS ---
    @FXML private Label lblVentas;
    @FXML private Label lblProductoMasVendido;
    @FXML private Label lblProductoMenosVendido;

    // --- SECCIÓN 2: INVENTARIO ---
    @FXML private Label lblTotalProductos;
    @FXML private Label lblStock;
    @FXML private Label lblValorInventario;
    @FXML private Label lblAgotados;

    // --- SECCIÓN 3: CLIENTES ---
    @FXML private Label lblClientesNuevos;
    @FXML private Label lblTopCliente;

    // --- UI GENERAL ---
    @FXML private Label lblBienvenido;
    @FXML private VBox panelResumenMes;
    @FXML private VBox panelInicio;
    @FXML private VBox panelClientesStats;
    @FXML private StackPane contenidoPrincipal;

    private AuthServicio authServicio;
    private final EstadisticaServicio estadisticaServicio = new EstadisticaServicio();
    private final NumberFormat formatoMoneda =
            NumberFormat.getCurrencyInstance(new Locale("es", "CR"));

    public void setAuthServicio(AuthServicio authServicio) {
        ejecutarSeguro(() -> {
            this.authServicio = authServicio;
            String username = authServicio.getUsuarioActivo().getUsername();
            lblBienvenido.setText("Bienvenido, " + username);
            actualizarTodo();
        });
    }

    private void actualizarTodo() {
        ejecutarSeguro(() -> {

            // Ventas
            lblVentas.setText(formatoMoneda.format(estadisticaServicio.getVentasDelMes()));
            lblProductoMasVendido.setText(estadisticaServicio.getProductoMasVendido());
            lblProductoMenosVendido.setText(estadisticaServicio.getProductoMenosVendido());

            // Inventario
            lblTotalProductos.setText(estadisticaServicio.getTotalProductosFisicos() + " uds.");
            lblStock.setText(String.valueOf(estadisticaServicio.getProductosPocosStock()));
            lblValorInventario.setText(formatoMoneda.format(estadisticaServicio.getValorTotalInventario()));
            lblAgotados.setText(String.valueOf(estadisticaServicio.getProductosAgotados()));

            // Clientes
            lblClientesNuevos.setText(String.valueOf(estadisticaServicio.getClientesNuevosMes()));
            lblTopCliente.setText(estadisticaServicio.getTopCliente());

        });
    }

    @FXML
    public void handleRegresarMain() {
        ejecutarSeguro(() -> {
            mostrarPaneles(true);
            contenidoPrincipal.setVisible(false);
            contenidoPrincipal.setManaged(false);
            actualizarTodo();
        });
    }

    private void cargarVista(String fxml) {
        ejecutarSeguro(() -> {
            URL url = getClass().getResource("/fxml/" + fxml);
            FXMLLoader loader = new FXMLLoader(url);
            Node vista = loader.load();

            contenidoPrincipal.getChildren().setAll(vista);

            mostrarPaneles(false);

            contenidoPrincipal.setVisible(true);
            contenidoPrincipal.setManaged(true);
        });
    }

    @FXML public void handleInventario() { cargarVista("Inventario.fxml"); }
    @FXML public void handleVentas()     { cargarVista("Ventas.fxml"); }
    @FXML public void handleClientes()   { cargarVista("Clientes.fxml"); }

    @FXML
    public void handleCerrarSesion() {
        ejecutarSeguro(() -> {
            authServicio.logout();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Stage stage = (Stage) lblBienvenido.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        });
    }

    // 🔹 Centraliza visibilidad de paneles
    private void mostrarPaneles(boolean visible) {
        lblBienvenido.setVisible(visible);
        lblBienvenido.setManaged(visible);
        panelResumenMes.setVisible(visible);
        panelResumenMes.setManaged(visible);
        panelInicio.setVisible(visible);
        panelInicio.setManaged(visible);
        panelClientesStats.setVisible(visible);
        panelClientesStats.setManaged(visible);
    }

    // 🔹 Manejo centralizado de errores
    private void ejecutarSeguro(Runnable accion) {
        try {
            accion.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}