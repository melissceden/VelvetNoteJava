package com.example.velvet_note.vista;

import com.example.velvet_note.dao.ProductoDAO;
import com.example.velvet_note.modelo.Producto;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class EliminarProductoController {

    @FXML private Label lblMensaje;
    @FXML private Label lblError;

    private Producto producto;

    // dao para eliminar el producto en la base de datos
    private ProductoDAO productoDAO = new ProductoDAO();

    // recibe el producto desde la vista anterior
    public void setProducto(Producto producto) {

        try {
            this.producto = producto;

            // muestra un mensaje con el nombre del producto
            lblMensaje.setText("¿Eliminar \"" + producto.getTitulo() + "\"?");

        } catch (Exception e) {
            System.out.println("Error al cargar producto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // se ejecuta cuando el usuario confirma eliminar
    @FXML
    public void handleEliminar() {

        try {
            // valida que exista un producto
            if (producto == null) {
                System.out.println("No hay producto seleccionado.");
                lblError.setText("Error: no hay producto seleccionado.");
                return;
            }

            System.out.println("ID a eliminar: " + producto.getProductoId());

            // intenta eliminar el producto
            boolean eliminado = productoDAO.eliminar(producto.getProductoId());

            if (eliminado) {
                System.out.println("Producto eliminado correctamente.");
                cerrarVentana();
            } else {
                lblError.setText("No se pudo eliminar el producto.");
            }

        } catch (Exception e) {
            // captura cualquier error inesperado
            System.out.println("Error al eliminar producto: " + e.getMessage());
            e.printStackTrace();
            lblError.setText("Error al eliminar.");
        }
    }

    // se ejecuta cuando el usuario cancela
    @FXML
    public void handleCancelar() {
        cerrarVentana();
    }

    // metodo para cerrar la ventana
    private void cerrarVentana() {
        Stage stage = (Stage) lblMensaje.getScene().getWindow();
        stage.close();
    }
}