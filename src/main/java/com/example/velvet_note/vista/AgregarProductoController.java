package com.example.velvet_note.vista;

import com.example.velvet_note.dao.ProductoDAO;
import com.example.velvet_note.modelo.Producto;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.math.BigDecimal;

public class AgregarProductoController {

    @FXML private TextField txtTitulo;
    @FXML private TextField txtArtista;
    @FXML private TextField txtGenero;
    @FXML private ComboBox<String> comboFormato;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;
    @FXML private Label lblError;

    // dao para guardar el producto en la base de datos
    private ProductoDAO productoDAO = new ProductoDAO();

    // se ejecuta automaticamente al cargar la vista
    @FXML
    public void initialize() {
        // agrega opciones al combobox
        comboFormato.getItems().addAll("Vinilo", "CD", "Casete");
    }

    // se ejecuta cuando el usuario presiona guardar
    @FXML
    public void handleGuardar() {

        try {
            // obtiene los datos del formulario
            String titulo   = txtTitulo.getText().trim();
            String artista  = txtArtista.getText().trim();
            String genero   = txtGenero.getText().trim();
            String formato  = comboFormato.getValue();
            String precioTxt = txtPrecio.getText().trim();
            String stockTxt  = txtStock.getText().trim();

            // valida que los campos obligatorios no esten vacios
            if (titulo.isEmpty() || artista.isEmpty() || formato == null
                    || precioTxt.isEmpty() || stockTxt.isEmpty()) {

                lblError.setText("Por favor completa todos los campos obligatorios.");
                return;
            }

            // convierte precio y stock a numeros
            BigDecimal precio = new BigDecimal(precioTxt);
            int stock = Integer.parseInt(stockTxt);

            // crea el objeto producto
            Producto nuevo = new Producto(
                    0,          // id (lo genera la base de datos)
                    titulo,
                    artista,
                    genero,
                    formato,
                    precio,
                    stock,
                    0           // total ventas inicial
            );

            // intenta guardar en la base de datos
            boolean guardado = productoDAO.agregar(nuevo);

            if (guardado) {
                System.out.println("Producto guardado correctamente.");
                cerrarVentana();
            } else {
                lblError.setText("No se pudo guardar el producto.");
            }

        } catch (NumberFormatException e) {
            // error si el usuario escribe texto en precio o stock
            System.out.println("Error de formato: " + e.getMessage());
            lblError.setText("Precio y stock deben ser números válidos.");

        } catch (Exception e) {
            // cualquier otro error (base de datos u otro)
            System.out.println("Error al guardar producto: " + e.getMessage());
            e.printStackTrace();
            lblError.setText("Error inesperado al guardar.");
        }
    }

    // metodo para cerrar la ventana
    private void cerrarVentana() {
        Stage stage = (Stage) txtTitulo.getScene().getWindow();
        stage.close();
    }

    // se ejecuta cuando el usuario presiona cancelar
    @FXML
    public void handleCancelar() {
        cerrarVentana();
    }
}