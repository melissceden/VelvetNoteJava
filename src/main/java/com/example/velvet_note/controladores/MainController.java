package com.example.velvet_note.controladores;

import com.example.velvet_note.servicio.AuthServicio;
import com.example.velvet_note.servicio.EstadisticaServicio;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ScrollBar;
import javafx.geometry.Orientation;
import java.util.Set;
import javafx.application.Platform;
import javafx.scene.control.Button; // Componente de botón interactivo
import javafx.scene.layout.Region; // Clase base de contenedores con dimensiones (VBox, HBox, etc.)

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;


public class MainController {

    @FXML private Label lblVentas;
    @FXML private Label lblProductoMasVendido;
    @FXML private Label lblProductoMenosVendido;

    @FXML private Label lblTotalProductos;
    @FXML private Label lblStock;
    @FXML private Label lblValorInventario;
    @FXML private Label lblAgotados;

    @FXML private Label lblClientesNuevos;
    @FXML private Label lblTopCliente;

    @FXML private Label lblBienvenido;
    @FXML private VBox panelResumenMes;
    @FXML private VBox panelInicio;
    @FXML private VBox panelClientesStats;
    @FXML private StackPane contenidoPrincipal;
    @FXML private ScrollPane scrollInicio;

    @FXML private Button btnInventario;
    @FXML private Button btnVentas;
    @FXML private Button btnClientes;

    private Button btnActivo = null;

    private void setBotonActivo(Button btn) {
        if (btnActivo != null) {
            btnActivo.getStyleClass().remove("menu-button-active");
            System.out.println("Removido de: " + btnActivo.getText()); // DEBUG
        }
        if (btn != null) {
            btn.getStyleClass().add("menu-button-active");
            System.out.println("Clases de " + btn.getText() + ": " + btn.getStyleClass()); // DEBUG
        }
        btnActivo = btn;
    }
    private AuthServicio authServicio;
    private final EstadisticaServicio estadisticaServicio = new EstadisticaServicio();
    private final NumberFormat formatoMoneda =
            NumberFormat.getCurrencyInstance(new Locale("es", "CR"));

    public void setAuthServicio(AuthServicio authServicio) {
        this.authServicio = authServicio;
        String username = authServicio.getUsuarioActivo().getUsername();
        lblBienvenido.setText("Bienvenido, " + username);
        actualizarTodo();
    }

    private void actualizarTodo() {
        var stats = estadisticaServicio;

        lblVentas.setText(formatoMoneda.format(stats.getVentasDelMes()));
        lblProductoMasVendido.setText(stats.getProductoMasVendido());
        lblProductoMenosVendido.setText(stats.getProductoMenosVendido());

        lblTotalProductos.setText(stats.getTotalProductosFisicos() + " uds.");
        lblStock.setText("" + stats.getProductosPocosStock());
        lblValorInventario.setText(formatoMoneda.format(stats.getValorTotalInventario()));
        lblAgotados.setText("" + stats.getProductosAgotados());

        lblClientesNuevos.setText("" + stats.getClientesNuevosMes());
        lblTopCliente.setText(stats.getTopCliente());
    }

    @FXML
    public void handleRegresarMain() { // Permite regresar a la vista principal ocultando sub-vistas
        contenidoPrincipal.setVisible(false); // Oculta el contenido dinámico cargado anteriormente
        contenidoPrincipal.setManaged(false);
        contenidoPrincipal.getChildren().clear(); // Libera memoria eliminando la vista anterior

        // Muestra nuevamente el dashboard completo
        scrollInicio.setVisible(true);
        scrollInicio.setManaged(true);

        setBotonActivo(null); // Quita el resaltado de todos los botones al volver al inicio
        actualizarTodo(); // Refresca la información
    }

    private void cargarVista(String fxml, Button boton) { // Carga dinámicamente una vista FXML dentro del contenedor principal
        try {
            URL url = getClass().getResource("/fxml/" + fxml); // Localiza el archivo FXML
            FXMLLoader loader = new FXMLLoader(url);                 // Prepara el cargador
            Parent vista = loader.load();                            // Carga la vista

            // Hace que la vista hija ocupe todo el alto disponible
            if (vista instanceof Region region) {
                region.setMaxWidth(Double.MAX_VALUE);
                region.setMaxHeight(Double.MAX_VALUE);
            }

            contenidoPrincipal.getChildren().setAll(vista);          // Inserta la nueva vista en el contenedor

            // Oculta el dashboard y muestra el módulo seleccionado
            scrollInicio.setVisible(false);
            scrollInicio.setManaged(false);
            contenidoPrincipal.setVisible(true);                     // Muestra el nuevo contenido
            contenidoPrincipal.setManaged(true);

            setBotonActivo(boton); // Resalta el botón correspondiente

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Métodos que responden a eventos de la UI
    @FXML public void handleInventario() { cargarVista("Inventario.fxml", btnInventario); }
    @FXML public void handleVentas()     { cargarVista("Ventas.fxml",     btnVentas); }
    @FXML public void handleClientes()   { cargarVista("Clientes.fxml",   btnClientes); }
    @FXML
    public void handleCerrarSesion() {
        try {
            authServicio.logout();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Stage stage = (Stage) lblBienvenido.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void initialize() {
        // Si el skin ya está listo lo aplicamos directo, si no esperamos
        if (scrollInicio.getSkin() != null) {
            aplicarEstiloScrollbar();
        } else {
            scrollInicio.skinProperty().addListener((obs, oldSkin, newSkin) -> {
                if (newSkin != null) aplicarEstiloScrollbar();
            });
        }
    }

    private void aplicarEstiloScrollbar() {
        Platform.runLater(() -> {
            Set<Node> scrollBars = scrollInicio.lookupAll(".scroll-bar");
            System.out.println("ScrollBars encontrados: " + scrollBars.size());
            for (Node node : scrollBars) {
                if (node instanceof ScrollBar sb && sb.getOrientation() == Orientation.VERTICAL) {
                    sb.setStyle("-fx-pref-width: 6;");
                    Node thumb = sb.lookup(".thumb");
                    System.out.println("Thumb: " + thumb);
                    if (thumb != null) {
                        thumb.setStyle("-fx-background-color: #3d3b6b; -fx-background-radius: 4;");
                    }
                    Node inc = sb.lookup(".increment-button");
                    Node dec = sb.lookup(".decrement-button");
                    if (inc != null) inc.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
                    if (dec != null) dec.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
                }
            }
        });
    }
}