package mx.uam.ayd.proyecto.presentacion.Eventos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
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
    private ListView<Evento> listaEventos;

    public void setControlador(ControlEventos controlador) {
        this.controlador = controlador;
    }

    public void inicia(List<Evento> event) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> inicia(event));
            return;
        }

        inicializarUI();

        if (listaEventos != null) {
            listaEventos.getItems().clear();
            if (event != null) {
                List<Evento> ordenados = new ArrayList<>(event);
                ordenados.sort(Comparator.comparing(Evento::getFechaE,
                        Comparator.nullsLast(Comparator.naturalOrder())));
                listaEventos.getItems().setAll(ordenados);
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
            configurarLista();

            scene = new Scene(root);
            stage.setScene(scene);

            initialized = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarLista() {
        listaEventos.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Evento e, boolean empty) {
                super.updateItem(e, empty);
                if (empty || e == null) {
                    setGraphic(null);
                    return;
                }

                Label titulo = new Label(e.getNombreEvento());
                titulo.getStyleClass().add("titulo-evento");

                GridPane datos = new GridPane();
                datos.setHgap(40);
                datos.setVgap(4);
                datos.addRow(0,
                        new Label("Lugar: " + e.getLugar()),
                        new Label("Fecha: " + e.getFechaE()),
                        new Label("Horario: " + e.getHoraIn() + " - " + e.getHoraFin()));
                datos.addRow(1,
                        new Label("Tipo: " + e.getTipoEvento()),
                        new Label("Acuerdo: " + e.getAcuerdoEconomico()),
                        new Label("Asistentes: " + e.getNoAsistentes()));
                datos.addRow(2,
                        new Label("Comisión: $" + e.getComision()),
                        new Label("Notificación: " + e.getNotificacion()));

                VBox tarjeta = new VBox(6, titulo, datos);
                tarjeta.setPadding(new Insets(8));
                setGraphic(tarjeta);
            }
        });
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