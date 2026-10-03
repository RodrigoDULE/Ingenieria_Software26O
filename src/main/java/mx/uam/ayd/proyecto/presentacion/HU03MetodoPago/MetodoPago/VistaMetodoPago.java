package mx.uam.ayd.proyecto.presentacion.HU03MetodoPago.MetodoPago;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Vista JavaFX encargada de capturar los datos del metodo de pago.
 * Gestiona la carga del FXML, la apertura de la ventana y la validacion de la
 * informacion ingresada por el usuario.
 */
@Component
public class VistaMetodoPago {

    private ControlMetodoPago controlador;
    private Stage stage;
    private Scene scenePrincipal;

    private boolean initialized = false;

    @FXML
    private TextField numeroTarjeta;
    @FXML
    private TextField nombreTarjeta;
    @FXML
    private TextField fechaExpiracion;
    @FXML
    private TextField cvv;

    /**
     * Crea una nueva instancia de la vista de metodo de pago.
     */
    public VistaMetodoPago() {
    }

    /**
     * Asigna el controlador que coordina el flujo de pago.
     *
     * @param control controlador asociado a esta vista.
     */
    public void setControlador(ControlMetodoPago control) {
        this.controlador = control;
    }

    /**
     * Inicializa la ventana de JavaFX una sola vez y carga el archivo FXML de la
     * pantalla de pago.
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
            stage.setTitle("Método de Pago");

            FXMLLoader loaderPrincipal = new FXMLLoader(
                    getClass().getResource("/fxml/ventana-metodo-pago.fxml"));
            loaderPrincipal.setController(this);
            scenePrincipal = new Scene(loaderPrincipal.load());

            initialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra la ventana de metodo de pago en pantalla.
     */
    public void muestra() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::muestra);
            return;
        }
        inicializarUI();
        stage.setScene(scenePrincipal);
        stage.show();
    }

    /**
     * Ejecuta la validacion de datos y, si es exitosa, crea la compra y
     * avanza al flujo de pagoaprobado.
     */
    @FXML
    public void realizarPago() {
        if (validaDatosTarjeta()) {
            if (controlador.simulacionConexionApi()) {
                controlador.crearCompra();
                controlador.iniciaVentanaAprobado();
                stage.close();
            } else {
                mostrarMensaje("Error al realizar el pago");
            }
        }
    }

    /**
     * Valida los datos ingresados por el usuario en el formulario de pago.
     *
     * @return true si todos los campos cumplen el formato esperado.
     */
    private boolean validaDatosTarjeta() {

        // Número de tarjeta: exactamente 16 dígitos
        if (!numeroTarjeta.getText().matches("\\d{16}")) {
            mostrarMensaje("El número de tarjeta debe tener 16 dígitos");
            return false;
        }

        // Nombre de la tarjeta
        if (nombreTarjeta.getText().isBlank()) {
            mostrarMensaje("El nombre no puede estar vacío");
            return false;
        }

        // El nombre no debe contener números
        if (nombreTarjeta.getText().matches(".*\\d.*")) {
            mostrarMensaje("El nombre no puede contener números");
            return false;
        }

        // Fecha de expiración: formato MM/AA
        if (!fechaExpiracion.getText().matches("(0[1-9]|1[0-2])/\\d{2}")) {
            mostrarMensaje("La fecha de expiración debe tener el formato MM/AA");
            return false;
        }

        // Obtener mes y año ingresados
        String[] partes = fechaExpiracion.getText().split("/");
        int mesIngresado = Integer.parseInt(partes[0]);
        int anioIngresado = Integer.parseInt(partes[1]);

        // Obtener mes y año actuales
        LocalDate hoy = LocalDate.now();
        int mesActual = hoy.getMonthValue();
        int anioActual = hoy.getYear() % 100;

        // Si el año es anterior, la tarjeta está vencida
        if (anioIngresado < anioActual) {
            mostrarMensaje("La fecha de expiración debe ser mayor a la actual");
            return false;
        }

        // Si es el mismo año pero el mes ya pasó, también está vencida
        if (anioIngresado == anioActual && mesIngresado < mesActual) {
            mostrarMensaje("La fecha de expiración debe ser mayor a la actual");
            return false;
        }

        // CVV: exactamente 3 dígitos
        if (!cvv.getText().matches("\\d{3}")) {
            mostrarMensaje("El CVV debe tener 3 dígitos");
            return false;
        }
        return true;
    }

    /**
     * Muestra un mensaje informativo en un cuadro de dialogo.
     *
     * @param mensaje texto que se presentara al usuario.
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
}
