package mx.uam.ayd.proyecto.presentacion.SerBot;

import java.io.IOException;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Component
public class VistaBot {
    private ControladorBot controladorBot;
    private Stage stage;
    private boolean Initialized = false;

    @FXML
    private TextField textMSG;
    
    @FXML
    private ListView<String> mensajesCont;

    public VistaBot() {
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventBot.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 600, 450);
            // scene.getStylesheets().add(getClass().getResource("/css/estilos-mezicuil.css").toExternalForm());
            stage.setScene(scene);
            textMSG.setText("");
            mensajesCont.getItems().add("Hola, soy el chatBot de Mezicuil, estoy aqui para ayudarte.");

            Initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void muestra(String Mensaje) {

        // this.control = control;
        System.out.println("Intentando abrir la ventana principal");
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.muestra(Mensaje));
            return;
        }


        inicializarUI();

        //Inicializamos el contenedor de mensajes
        
        if(Mensaje != null){
            //despues metemos el mensaje que viene de externos    
            mensajesCont.getItems().add("-" + Mensaje);
        }

        stage.show();

    }

    // creamos un setter para establecer un controlador a la vista
    public void setControlador(ControladorBot control) {
        this.controladorBot = control;
    }

    @FXML
    private void handleSendMessage() {
        String mensaje = textMSG.getText();
        muestra(mensaje);
        if (controladorBot != null) {
            controladorBot.recibeMensaje(mensaje);
        }
    }
}
