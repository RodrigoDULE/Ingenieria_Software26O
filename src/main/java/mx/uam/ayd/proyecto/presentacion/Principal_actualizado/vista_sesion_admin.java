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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;

@Component
public class vista_sesion_admin {

    private controlador_sesion_admin control;
    private boolean Initialized = false;
    private Stage stage;

    // Inicializo los botones que aparecen al incio de la app, vamos a hacer que la
    // app interactue con ellos
    @FXML
    private Button IngresarTienda; //

    @FXML
    private Button ingresarFormularioMarketing;
    @FXML
    private Button RevisionPedidos;

    @FXML
    private Button AltaProducto;

    @FXML
    private Button AgregarEvento;//

    @FXML
    private TextField ingresarUsuario;

    @FXML
    private Text textoPrincipal;

    @FXML 
    private TextField txtOrdenId;

    @FXML 
    private PasswordField ingresarContrasena; 

    //@FXML
    //private Text TextoRecepcion;
   

    public vista_sesion_admin() {
    }

    // inicializa la vista principal con la que el usuario interactuara
    private void inicializarUI() {
        if (Initialized) {
            return;
        }

        // Create UI only if we're on JavaFX thread
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::inicializarUI);
            return;
        }

        try {
            stage = new Stage();
            stage.setTitle("Mi Aplicación");

            // Load FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-sesion-admin.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 600, 450);
            // scene.getStylesheets().add(getClass().getResource("/css/estilos-mezicuil.css").toExternalForm());
            stage.setScene(scene);

            Initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // creamos un setter para establecer un controlador a la vista
    public void setControlPrincipal(controlador_sesion_admin control) {
        this.control = control;
    }

    // metodo para mostrar la ventana principal
    public void muestra(String NombreUsuario) {

        if (NombreUsuario == null) {

            // this.control = control;
            System.out.println("Intentando abrir la ventana principal");
            if (!Platform.isFxApplicationThread()) {
                Platform.runLater(() -> this.muestra(null));
                return;
            }

            inicializarUI();

            // Deshabilitamos los botones al inicio del mundo
            IngresarTienda.setDisable(true);
            AgregarEvento.setDisable(true);
            RevisionPedidos.setVisible(false);
            AltaProducto.setVisible(false);
            ingresarFormularioMarketing.setVisible(false);
            txtOrdenId.setVisible(false);
            //TextoRecepcion.setVisible(false);

            ingresarUsuario.setText("Predeterminado");
            stage.show();
            
        } else {
            System.out.println("Intentando abrir la ventana principal");
            if (!Platform.isFxApplicationThread()) {
                Platform.runLater(() -> this.muestra(NombreUsuario));
                return;
            }

            inicializarUI();
            // Deshabilitamos los botones al inicio del mundo
            IngresarTienda.setDisable(false);
            AgregarEvento.setDisable(false);
            RevisionPedidos.setVisible(true);
            AltaProducto.setVisible(true);
            //TextoRecepcion.setVisible(true);
            ingresarFormularioMarketing.setVisible(true);
            ingresarUsuario.setDisable(false);
            txtOrdenId.setVisible(true);
            textoPrincipal.setText("Bienvenido, " + NombreUsuario);
            System.out.println(NombreUsuario);
            stage.show();
        }

    }

    // Inicializamos los metodos de los botones de la ventana principal
    @FXML
    private void handleVisitarTiendaLinea() {
        System.out.println("Boton presionado");
        if (control != null) {
            control.visitaTiendaLinea();
        }
    }

    @FXML
    private void revisarInventario() {
        if (control != null) {
            control.revisarInventario();
        } else {
            System.err.println("Error: controladorPrincipal no está inyectado.");
        }
    }

    @FXML
    private void agendarEvento() {
        System.out.println("Boton presionado");
        if (control != null) {
            control.Eventos();
        } else {
            System.err.println("Error: controladorPrincipal no está inyectado.");
        }
    }

    @FXML
    private void handleAbrirMarketing() {
        if (control != null) {
            control.abreFormularioMarketing();
        } else {
            System.err.println("Error: controladorPrincipal no está inyectado.");
        }
    }

    @FXML
    private void ingresarUsuario() {
        String nombre = ingresarUsuario.getText();
        String contrasena = ingresarContrasena.getText();

        if (nombre == null || nombre.isBlank() || contrasena == null || contrasena.isBlank()) {
            mostrarMensaje("Ingresa tu usuario y contraseña.");
            return;
        }
        if (control != null) {
            control.buscaAdmin(nombre, contrasena);   // dos argumentos
        }
    }


    // mostrar mensaje
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

    // Boton para HU-6
    @FXML
    public void iniciarRevisionOrdenes() {
        if (control != null) {
            control.irAVentanaOrdenesCreadas();
        }
    }

    // Boton para HU7
    @FXML
    public void iniciarRecepcionCarga()
    {
        String id = txtOrdenId.getText().trim();

        if(!id.isEmpty())
        {
            control.iniciarRecepcion(id);
            txtOrdenId.clear();
        }
    }

}