package com.example.velvet_note.controladores;

import com.example.velvet_note.servicio.AuthServicio;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

// controlador del login
public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;
    @FXML private Button btnLogin;

    // servicio de autenticacion
    private AuthServicio authServicio = new AuthServicio();

    // se ejecuta cuando el usuario presiona el boton login
    @FXML
    public void handleLogin() {

        try {
            // obtener datos del formulario
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();

            System.out.println("Intentando login con: " + username);

            // validar campos vacios
            if (username.isEmpty() || password.isEmpty()) {
                lblError.setText("Completa todos los campos.");
                return;
            }

            // intentar login
            boolean acceso = authServicio.login(username, password);

            if (acceso) {
                System.out.println("Login correcto.");
                abrirMain();
            } else {
                System.out.println("Credenciales incorrectas.");
                lblError.setText("Usuario o contraseña incorrectos");
            }

        } catch (Exception e) {
            // cualquier error inesperado
            System.out.println("Error en login: " + e.getMessage());
            e.printStackTrace();
            lblError.setText("Error inesperado.");
        }
    }

    // abre la pantalla principal
    private void abrirMain() {

        try {
            System.out.println("Abriendo pantalla principal...");

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/Main.fxml")
            );

            Parent root = loader.load();

            MainController controller = loader.getController();
            controller.setAuthServicio(authServicio);

            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();

        } catch (Exception e) {
            System.out.println("Error al abrir Main: " + e.getMessage());
            e.printStackTrace();
            lblError.setText("No se pudo abrir la aplicación.");
        }
    }
}