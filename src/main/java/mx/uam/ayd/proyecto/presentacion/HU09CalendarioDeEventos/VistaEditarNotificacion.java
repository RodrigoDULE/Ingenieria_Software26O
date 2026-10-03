package mx.uam.ayd.proyecto.presentacion.HU09CalendarioDeEventos;

import java.time.LocalDate;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Evento;

@Component
public class VistaEditarNotificacion {

    // Inyección de los fx:id de tu nuevo FXML
    @FXML private DatePicker dpFecha;
    @FXML private Spinner<Integer> spDias;
    @FXML private ToggleButton tgDias;
    @FXML private Spinner<Integer> spSemanas;
    @FXML private ToggleButton tgSemanas;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private Stage stage;
    private boolean inicializado = false;
    private ControladorCalendarioEventos controlador;
    private Evento eventoSeleccionado;

    public void setControlador(ControladorCalendarioEventos controlador) {
        this.controlador = controlador;
    }

    private void inicializarUI() {
        if (inicializado) return;

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::inicializarUI);
            return;
        }

        try {
            stage = new Stage();
            stage.setTitle("Editar Notificación");
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana de atrás

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-notificaciones-calendario-evento.fxml"));
            loader.setController(this);

            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            // Configurar rangos de los Spinners
            spDias.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, 1));
            spSemanas.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 52, 1));

            // Eventos de botones
            btnCancelar.setOnAction(e -> stage.close());
            btnGuardar.setOnAction(e -> guardar());

            inicializado = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void muestra(Evento evento) {
        this.eventoSeleccionado = evento;
        inicializarUI();

        if (evento != null && dpFecha != null) {
            dpFecha.setValue(evento.getNotificacion() != null ? evento.getNotificacion() : LocalDate.now());
        }

        stage.showAndWait();
    }

   private void guardar() {
    if (controlador != null && eventoSeleccionado != null) {
            // Extraemos los datos de la UI
            LocalDate nuevaFechaNotificacion = dpFecha.getValue();
            int dias = spDias.getValue();
            int semanas = spSemanas.getValue();

            // Llamamos al controlador con los 4 parámetros en orden
            controlador.guardarNotificacion(eventoSeleccionado, dias, semanas, nuevaFechaNotificacion);
        }
        stage.close(); // Cerramos la ventana emergente
    }
}
