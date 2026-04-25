package com.example.velvet_note.vista;

import com.example.velvet_note.servicio.AuthServicio;         // Accede a servicio de autenticacion
import com.example.velvet_note.servicio.EstadisticaServicio;  // Servicio de Estadisticas
import javafx.fxml.FXML;              // Indica a JavaFX qué atributos/metodos estan vinculados al FXML
import javafx.fxml.FXMLLoader;        // Carga archivos FXML convirtiendolos en objetos Java
import javafx.scene.Node;             // Clase base de todos los elementos visuales
import javafx.scene.Scene;            // Representa el contenido completo de una ventana
import javafx.scene.control.Label;    // Componente para mostrar texto en la interfaz
import javafx.scene.layout.StackPane; // Apila elementos uno encima de otro
import javafx.scene.layout.VBox;      // Organiza elementos en forma vertical
import javafx.stage.Stage;

import java.io.IOException;           // Necesaria para manejar errores al cargar vistas
import java.net.URL;                  // Permite localizar recursos dentro del proyecto
import java.text.NumberFormat;        // Permite formatear numeros (ej: moneda)
import java.util.Locale;              // Define configuración regional

public class MainController {

    // SECCIÓN VENTAS, Muestra métricas principales de ventas
    @FXML private Label lblVentas;
    @FXML private Label lblProductoMasVendido;
    @FXML private Label lblProductoMenosVendido;

    // SECCIÓN INVENTARIO, Estado actual de productos
    @FXML private Label lblTotalProductos;
    @FXML private Label lblStock;
    @FXML private Label lblValorInventario;
    @FXML private Label lblAgotados;

    // SECCIÓN CLIENTES, Información relevante de clientes
    @FXML private Label lblClientesNuevos;
    @FXML private Label lblTopCliente;

    // UI GENERAL, Elementos que controlan la navegación y visibilidad
    @FXML private Label lblBienvenido;
    @FXML private VBox panelResumenMes;
    @FXML private VBox panelInicio;
    @FXML private VBox panelClientesStats;
    @FXML private StackPane contenidoPrincipal;


    private AuthServicio authServicio; // Servicio de autenticación

    // Servicio que proporciona todos los datos estadísticos
    private final EstadisticaServicio estadisticaServicio = new EstadisticaServicio();

    private final NumberFormat formatoMoneda = // Formateador de moneda con configuración de Costa Rica
            NumberFormat.getCurrencyInstance(new Locale("es", "CR"));

    // Recibe el servicio de autenticación y configura la bienvenida del usuario
    public void setAuthServicio(AuthServicio authServicio) {
        this.authServicio = authServicio;

        String username = authServicio.getUsuarioActivo().getUsername(); // Obtiene el usuario activo
        lblBienvenido.setText("Bienvenido, " + username);                // Muestra el usuario

        // Carga todos los datos iniciales en la interfaz
        actualizarTodo();
    }

    // Funciona como puente entre la lógica (servicio) y la vista (labels)
    private void actualizarTodo() {
        var stats = estadisticaServicio;   // Se usa una referencia local

        lblVentas.setText(formatoMoneda.format(stats.getVentasDelMes()));    // VENTAS
        lblProductoMasVendido.setText(stats.getProductoMasVendido());
        lblProductoMenosVendido.setText(stats.getProductoMenosVendido());

        lblTotalProductos.setText(stats.getTotalProductosFisicos() + " uds.");    // INVENTARIO
        lblStock.setText("" + stats.getProductosPocosStock());                    // Convertido a texto
        lblValorInventario.setText(formatoMoneda.format(stats.getValorTotalInventario()));
        lblAgotados.setText("" + stats.getProductosAgotados());

        lblClientesNuevos.setText("" + stats.getClientesNuevosMes());        // CLIENTES
        lblTopCliente.setText(stats.getTopCliente());
    }


    @FXML
    public void handleRegresarMain() { // Permite regresar a la vista principal ocultando sub-vistas
        mostrarPaneles(true);    // Muestra nuevamente los paneles principales
        contenidoPrincipal.setVisible(false); // Oculta el contenido dinámico cargado anteriormente
        contenidoPrincipal.setManaged(false);
        actualizarTodo();             // Refresca la información
    }


    private void cargarVista(String fxml) { // Carga dinámicamente una vista FXML dentro del contenedor principal
        try {
            URL url = getClass().getResource("/fxml/" + fxml); // Localiza el archivo FXML
            FXMLLoader loader = new FXMLLoader(url);                 // Prepara el cargador
            Node vista = loader.load();                              // Carga la vista
            contenidoPrincipal.getChildren().setAll(vista);          // Inserta la nueva vista en el contenedor
            mostrarPaneles(false);                             // Oculta paneles principales
            contenidoPrincipal.setVisible(true);                     // Muestra el nuevo contenido
            contenidoPrincipal.setManaged(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Métodos que responden a eventos de la UI
    @FXML public void handleInventario() { cargarVista("Inventario.fxml"); }
    @FXML public void handleVentas()     { cargarVista("Ventas.fxml"); }
    @FXML public void handleClientes()   { cargarVista("Clientes.fxml"); }

    // Cierra la sesión actual y redirige al login
    @FXML
    public void handleCerrarSesion() {

        try {
            authServicio.logout(); // Cierra la sesión del usuario
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml")); // Carga la vista de login
            Stage stage = (Stage) lblBienvenido.getScene().getWindow(); // Obtiene la ventana actual
            stage.setScene(new Scene(loader.load())); // Reemplaza la escena

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarPaneles(boolean visible) { // Controla la visibilidad de los paneles principales

        // visible: controla si se ve o no
        // managed: controla si ocupa espacio en el layout

        lblBienvenido.setVisible(visible);
        lblBienvenido.setManaged(visible);

        panelResumenMes.setVisible(visible);
        panelResumenMes.setManaged(visible);

        panelInicio.setVisible(visible);
        panelInicio.setManaged(visible);

        panelClientesStats.setVisible(visible);
        panelClientesStats.setManaged(visible);
    }
}