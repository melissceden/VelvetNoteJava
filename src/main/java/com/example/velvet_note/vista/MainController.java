package com.example.velvet_note.vista;

import com.example.velvet_note.servicio.AuthServicio;
import com.example.velvet_note.servicio.EstadisticaServicio;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
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
    @FXML private Label lblStock; // Poco stock
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
    private EstadisticaServicio estadisticaServicio = new EstadisticaServicio();
    private final NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CR"));

    public void setAuthServicio(AuthServicio authServicio) {
        try {
            this.authServicio = authServicio;
            String username = authServicio.getUsuarioActivo().getUsername();
            lblBienvenido.setText("Bienvenido, " + username);
            actualizarTodo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarTodo() {
        try {
            // 1. Ventas del mes
            lblVentas.setText(formatoMoneda.format(estadisticaServicio.getVentasDelMes()));
            lblProductoMasVendido.setText(estadisticaServicio.getProductoMasVendido());
            lblProductoMenosVendido.setText(estadisticaServicio.getProductoMenosVendido());

            // 2. Inventario
            lblTotalProductos.setText(estadisticaServicio.getTotalProductosFisicos() + " uds.");
            lblStock.setText(String.valueOf(estadisticaServicio.getProductosPocosStock()));
            lblValorInventario.setText(formatoMoneda.format(estadisticaServicio.getValorTotalInventario()));
            lblAgotados.setText(String.valueOf(estadisticaServicio.getProductosAgotados()));

            // 3. Clientes
            lblClientesNuevos.setText(String.valueOf(estadisticaServicio.getClientesNuevosMes()));
            lblTopCliente.setText(estadisticaServicio.getTopCliente());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRegresarMain() {
        lblBienvenido.setVisible(true);
        lblBienvenido.setManaged(true);
        panelResumenMes.setVisible(true);
        panelResumenMes.setManaged(true);
        panelInicio.setVisible(true);
        panelInicio.setManaged(true);
        panelClientesStats.setVisible(true);
        panelClientesStats.setManaged(true);

        contenidoPrincipal.setVisible(false);
        contenidoPrincipal.setManaged(false);

        actualizarTodo();
    }

    private void cargarVista(String fxml) {
        try {
            URL url = getClass().getResource("/fxml/" + fxml);
            FXMLLoader loader = new FXMLLoader(url);
            Node vista = loader.load();

            contenidoPrincipal.getChildren().setAll(vista);

            // Ocultar paneles de estadísticas
            lblBienvenido.setVisible(false);
            lblBienvenido.setManaged(false);
            panelResumenMes.setVisible(false);
            panelResumenMes.setManaged(false);
            panelInicio.setVisible(false);
            panelInicio.setManaged(false);
            panelClientesStats.setVisible(false);
            panelClientesStats.setManaged(false);

            contenidoPrincipal.setVisible(true);
            contenidoPrincipal.setManaged(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void handleInventario() { cargarVista("Inventario.fxml"); }
    @FXML public void handleVentas()     { cargarVista("Ventas.fxml"); }
    @FXML public void handleClientes()   { cargarVista("Clientes.fxml"); }

    @FXML
    public void handleCerrarSesion() {
        try {
            authServicio.logout();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Stage stage = (Stage) lblBienvenido.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}