package mx.uam.ayd.proyecto.presentacion.Principal_actualizado;

import java.io.IOException;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Stage;

@Component
public class vistaRol {

    private controladorRol control;
    private boolean initialized = false;
    private Stage stage;

    @FXML
    private Text textoPrincipal;

    @FXML
    private RadioButton rbAdministrador;

    @FXML
    private RadioButton rbUsuario;

    @FXML
    private ToggleGroup grupoRol;

    @FXML
    private Button btnAceptar;

    public vistaRol() {
    }

    // JavaFX llama a este metodo despues de cargar el FXML
    @FXML
    private void initialize() {
        if (btnAceptar != null) {
            btnAceptar.setOnAction(event -> aceptar());
        }
    }

    // crea la ventana una sola vez, siempre en el hilo de JavaFX
    private void inicializarUI() {
        if (initialized) return;

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::inicializarUI);
            return;
     }

        try {
            stage = new Stage();
            stage.setTitle("Portal Mezicuil");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana_rol_principal.fxml"));
            loader.setController(this); // Asignación manual
        
            Scene scene = new Scene(loader.load(), 600, 450);
            stage.setScene(scene);

            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setControlSeleccionRol(controladorRol control) {
        this.control = control;
    }

    // muestra la ventana de seleccion de rol
    public void muestra() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::muestra);
            return;
        }

        inicializarUI();
        rbAdministrador.setSelected(true);
        stage.show();
    }

    // cierra la ventana de seleccion de rol
    public void cierra() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::cierra);
            return;
        }

        if (stage != null) {
            stage.hide();
        }
    }

    // se ejecuta al presionar "Aceptar"
    // Agrega @FXML arriba del método aceptar
    @FXML
    private void aceptar() {
        System.out.println("Botón Aceptar presionado"); // Mensaje de depuración en consola

        if (control == null) {
            System.err.println("Error: controladorRol no está inyectado.");
            return;
        }

        // Validación por si no están en ToggleGroup o se evalúan individualmente
        if (rbAdministrador != null && rbAdministrador.isSelected()) {
            control.ingresaComoAdministrador();
        } else if (rbUsuario != null && rbUsuario.isSelected()) {
            control.ingresaComoCliente();
        } else {
            mostrarMensaje("Por favor, selecciona un perfil para continuar.");
        }
    }

    public void mostrarMensaje(String mensaje) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.mostrarMensaje(mensaje));
            return;
        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}