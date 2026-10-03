package mx.uam.ayd.proyecto.presentacion.Eventos;

import java.util.List;

import org.springframework.stereotype.Component;

import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Evento;

@Component
public class VistaEventos {

    private ControlEventos controlador;

    private Stage stage;
    private Scene scene;

    private boolean initialized = false;

    @FXML
    private Button btnNuevoEvento;

    @FXML
    private Button btnCalendario;

    @FXML
    private FlowPane contenedorEventos;

    public void setControlador(ControlEventos controlador) {
        this.controlador = controlador;
    }

    public void inicia(List<Evento> event) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> inicia(event));
            return;
        }

        inicializarUI();

        if (contenedorEventos != null) {
            contenedorEventos.getChildren().clear();

            if (event != null) {
                for (Evento e : event) {

                    VBox tarjeta = new VBox(5);
                    tarjeta.getStyleClass().add("event-card");

                    Label nombre = new Label(e.getNombreEvento());
                    nombre.getStyleClass().add("titulo-evento");

                    Label lugar = new Label("Lugar: " + e.getLugar());

                    Label comision = new Label("Comisión: $" + e.getComision());

                    Label asistentes = new Label("Asistentes: " + e.getNoAsistentes());

                    Label fecha = new Label("Fecha: " + e.getFechaE());

                    Label horario = new Label(
                            "Horario: " + e.getHoraIn() + " - " + e.getHoraFin());

                    Label notificacion = new Label(
                            "Notificación: " + e.getNotificacion());

                    Label tipoEvento = new Label("Tipo de Evento: " + e.getTipoEvento());

                    Label acuerdoEconomico = new Label("Acuerdo Económico: " + e.getAcuerdoEconomico());

                    tarjeta.getChildren().addAll(
                            nombre,
                            lugar,
                            comision,
                            asistentes,
                            fecha,
                            horario,
                            notificacion,
                            tipoEvento,
                            acuerdoEconomico);

                    contenedorEventos.getChildren().add(tarjeta);
                }
            }
        }

        if (stage != null) {
            stage.show();
            stage.toFront();
            stage.requestFocus();
        }
    }

    private void inicializarUI() {
        if (initialized) {
            return;
        }

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::inicializarUI);
            return;
        }

        try {
            stage = new Stage();
            stage.setTitle("Eventos");

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/ventana-Eventos.fxml"));

            loader.setController(this);

            Pane root = loader.load();

            scene = new Scene(root);
            stage.setScene(scene);

            initialized = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirAgendarEvento() {
        controlador.abrirAgendarEvento();

    }

    @FXML
    private void abrirCalendario() {
        controlador.abrirCalendario();
    }

}
