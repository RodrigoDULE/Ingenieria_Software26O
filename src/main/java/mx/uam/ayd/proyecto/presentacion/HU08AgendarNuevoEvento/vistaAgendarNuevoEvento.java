package mx.uam.ayd.proyecto.presentacion.HU08AgendarNuevoEvento;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Vista JavaFX para agendar un nuevo evento.
 *
 * <p>Gestiona la captura de datos en la interfaz, valida campos y delega en el
 * controlador la lógica de negocio para registrar eventos.</p>
 */
@Component
public class vistaAgendarNuevoEvento {

    private boolean initialized = false;
    private controlAgendarNuevoEvento controlAgendarNuevoEvento;
    private Stage stage;
    private Scene scenePrincipal;

    @FXML
    private TextField txtNombreEvento;
    @FXML
    private ComboBox<String> cmbTipoEvento;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private TextField txtHoraInicio;
    @FXML
    private TextField txtHoraFin;
    @FXML
    private ComboBox<String> cmbAcuerdo;
    @FXML
    private TextField txtLugar;
    @FXML
    private TextArea txtNotas;
    @FXML
    private ComboBox<String> cmbEmpleado;
    @FXML
    private ListView<String> lvEmpleados;

    /**
     * Inicializa los componentes visuales de la ventana.
     *
     * <p>Carga los catálogos estáticos para tipo de evento y tipo de acuerdo.</p>
     */
    public void initialize() {

        cmbTipoEvento.getItems().addAll(
                "Conferencia",
                "Reunión",
                "Taller");

        cmbAcuerdo.getItems().addAll(
                "Gratis",
                "Pago");
    }

    /**
     * Carga los nombres de empleados en el {@code ComboBox} desde la base de
     * datos.
     */
    public void cargarEmpleados() {
        List<String> nombres = controlAgendarNuevoEvento.obtenerNombresEmpleados();
        cmbEmpleado.getItems().clear();
        cmbEmpleado.getItems().addAll(nombres);
    }

    /**
     * Constructor por defecto de la vista.
     */
    public vistaAgendarNuevoEvento() {
    }

    /**
     * Inyecta el controlador asociado a este caso de uso.
     *
     * @param controlador controlador de agendar nuevo evento
     */
    public void setControladorAgendarNuevoEvento(controlAgendarNuevoEvento controlador) {
        this.controlAgendarNuevoEvento = controlador;
    }

    /**
     * Inicializa la interfaz gráfica de la ventana principal.
     *
     * <p>Si la llamada ocurre fuera del hilo de JavaFX, se reprograma su
     * ejecución en el hilo correcto.</p>
     */
    private void inicializarUI() {
        // Si la UI ya está inicializada, no hacer nada
        if (initialized) {
            return;
        }
        // Si no estamos en el hilo de JavaFX, pedir que lo haga
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::inicializarUI);
            return;
        }
        try {
            stage = new Stage();
            stage.setTitle("Agendar Nuevo Evento");

            FXMLLoader loaderPrincipal = new FXMLLoader(
                    getClass().getResource("/fxml/ventana-agendar-evento.fxml"));
            loaderPrincipal.setController(this);
            scenePrincipal = new Scene(loaderPrincipal.load());

            initialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra la ventana principal para agendar eventos.
     *
     * <p>Garantiza ejecución en el hilo de JavaFX, inicializa la UI cuando es
     * necesario y recarga la lista de empleados.</p>
     */
    public void muestra() {
        // Si no estamos en el hilo de JavaFX, pedir que lo haga
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::muestra);
            return;
        }
        // Inicializar la UI si no lo está
        inicializarUI();
        // Cargar empleados desde la BD cada vez que se abre la ventana
        cargarEmpleados();
        // Establecer la escena principal
        stage.setScene(scenePrincipal);
        // Mostrar la ventana
        stage.show();
    }

    /**
     * Verifica que todos los campos requeridos tengan valores válidos.
     *
     * @return {@code true} si la captura es válida; en caso contrario,
     *         {@code false}
     */
    public boolean verificarCampos() {
        if (txtNombreEvento.getText().isEmpty()) {
            mostrarMensaje("Debe ingresar un nombre para el evento");
            return false;
        }
        if (cmbTipoEvento.getValue() == null) {
            mostrarMensaje("Debe seleccionar un tipo de evento");
            return false;
        }
        if (dpFecha.getValue() == null) {
            mostrarMensaje("Debe seleccionar una fecha");
            return false;
        }

        try {
            LocalTime.parse(txtHoraInicio.getText());
        } catch (DateTimeParseException e) {
            mostrarMensaje("La hora de inicio debe tener el formato HH:mm");

            return false;
        }

        try {
            LocalTime.parse(txtHoraFin.getText());
        } catch (DateTimeParseException e) {
            mostrarMensaje("La hora de finalización debe tener el formato HH:mm");
            return false;
        }

        LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText());
        LocalTime horaFin = LocalTime.parse(txtHoraFin.getText());

        if (!horaFin.isAfter(horaInicio)) {
            mostrarMensaje("La hora de finalización debe ser posterior a la de inicio");
            return false;
        }

        if (cmbAcuerdo.getValue() == null) {
            mostrarMensaje("Debe seleccionar un acuerdo");
            return false;
        }
        if (txtLugar.getText().isEmpty()) {
            mostrarMensaje("Debe ingresar un lugar");
            return false;
        }
        if (lvEmpleados.getItems().isEmpty()) {
            mostrarMensaje("Debe seleccionar al menos un empleado");
            return false;
        }

        return true;
    }

    /**
     * Agrega el empleado seleccionado a la lista de participantes del evento.
     *
     * <p>Evita duplicados y notifica cuando un empleado ya fue agregado.</p>
     */
    @FXML
    public void agregarEmpleado() {
        String empleado = cmbEmpleado.getValue();
        if (empleado != null) {
            if (lvEmpleados.getItems().contains(empleado)) {
                mostrarMensaje("El empleado \"" + empleado + "\" ya fue agregado.");
            } else {
                lvEmpleados.getItems().add(empleado);
                cmbEmpleado.setValue(null);
            }
        }
    }

    /**
     * Cancela la captura del evento actual, limpia el formulario y cierra la
     * ventana.
     */
    @FXML
    public void cancelarEvento() {
        limpiarCampos();
        stage.close();
    }

    /**
     * Guarda el evento cuando los campos son válidos y existe disponibilidad en
     * el horario seleccionado.
     */
    @FXML
    public void guardarEvento() {
        if (verificarCampos()) {
            if (controlAgendarNuevoEvento.verificarDisponibilidad(dpFecha.getValue(),
                    LocalTime.parse(txtHoraInicio.getText()), LocalTime.parse(txtHoraFin.getText()))) {
                mostrarMensaje("Evento guardado exitosamente");
                controlAgendarNuevoEvento.agregarEvento(txtNombreEvento.getText(), cmbTipoEvento.getValue(),
                        dpFecha.getValue(),
                        txtHoraInicio.getText(), txtHoraFin.getText(), cmbAcuerdo.getValue(),
                    txtLugar.getText(), txtNotas.getText(), lvEmpleados.getItems().size(),
                    (lvEmpleados.getItems().size() * 50) + 2500,
                    lvEmpleados.getItems());
                limpiarCampos();
                stage.close();
            } else {
                mostrarMensaje("Evento no cumple con condiciones.");
            }
        }
    }

    /**
     * Muestra un mensaje informativo en un cuadro de diálogo.
     *
     * @param mensaje texto a mostrar al usuario
     */
    private void mostrarMensaje(String mensaje) {
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

    /**
     * Limpia los campos del formulario para dejar la vista en su estado inicial.
     */
    private void limpiarCampos() {
        txtNombreEvento.clear();
        dpFecha.setValue(null);
        txtHoraInicio.clear();
        txtHoraFin.clear();
        txtLugar.clear();
        txtNotas.clear();
        lvEmpleados.getItems().clear();
    }
}