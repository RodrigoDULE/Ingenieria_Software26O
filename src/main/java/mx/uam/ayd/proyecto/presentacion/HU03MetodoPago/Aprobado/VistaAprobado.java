package mx.uam.ayd.proyecto.presentacion.HU03MetodoPago.Aprobado;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

@Component
/**
 * Vista JavaFX encargada de mostrar la confirmacion del pago aprobado.
 * Administra la carga del FXML, la inicializacion de la ventana y la
 * presentacion de los datos de la transaccion.
 */
public class VistaAprobado {

    private boolean initialized = false;
    private ControlAprobado controlador;
    private Stage stage;
    private Scene scenePrincipal;
    @FXML
    private Label lblFecha;

    @FXML
    private Label lblMetodo;

    @FXML
    private Label lblMonto;

    /**
     * Crea una nueva instancia de la vista de aprobacion.
     */
    public VistaAprobado() {
    }

    /**
     * Asigna el controlador asociado a esta vista.
     *
     * @param controlador controlador que provee los datos de la pantalla.
     */
    public void setControlador(ControlAprobado controlador) {
        this.controlador = controlador;
    }

    /**
     * Inicializa la interfaz grafica una sola vez y prepara la escena principal.
     * Si la llamada ocurre fuera del hilo de JavaFX, reprograma la ejecucion en
     * el hilo correcto.
     */
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
            stage.setTitle("Pago Aprobado");

            FXMLLoader loaderPrincipal = new FXMLLoader(
                    getClass().getResource("/fxml/ventana_Aprobado.fxml"));
            loaderPrincipal.setController(this);
            scenePrincipal = new Scene(loaderPrincipal.load());

            initialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra la ventana de aprobacion con la fecha actual y el monto recibido
     * desde el controlador.
     */
    public void muestra() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::muestra);
            return;
        }
        inicializarUI();
        mostrarDatos(LocalDate.now(), controlador.getMonto());
        stage.setScene(scenePrincipal);
        stage.show();
    }

    /**
     * Rellena los campos visuales con la fecha, el metodo de pago y el monto de
     * la transaccion.
     *
     * @param fecha fecha de la transaccion.
     * @param monto monto total aprobado.
     */
    public void mostrarDatos(LocalDate fecha, double monto) {

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        lblFecha.setText(fecha.format(formato));
        lblMetodo.setText("Tarjeta de Crédito");
        lblMonto.setText(String.format("$%,.2f", monto));
    }

    /**
     * Cierra la ventana de aprobacion y regresa al flujo anterior.
     */
    @FXML
    private void handleVolverInicio() {
        stage.close();
    }
}