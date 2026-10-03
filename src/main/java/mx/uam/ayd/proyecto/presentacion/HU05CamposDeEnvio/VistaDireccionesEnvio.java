package mx.uam.ayd.proyecto.presentacion.HU05CamposDeEnvio;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.DireccionEnvio;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.DireccionEnvio.DatosDireccion;


// Administra dos escenas (selección de dirección y formulario de nueva dirección) dentro del mismo Stage.
@Component
public class VistaDireccionesEnvio {

    // Lista inmutable de estados para llenar el ComboBox del formulario.
    private static final List<String> ESTADOS = Arrays.asList(
            "Aguascalientes", "Baja California", "Baja California Sur", "Campeche",
            "Chiapas", "Chihuahua", "Ciudad de México", "Coahuila", "Colima",
            "Durango", "Estado de México", "Guanajuato", "Guerrero", "Hidalgo",
            "Jalisco", "Michoacán", "Morelos", "Nayarit", "Nuevo León", "Oaxaca",
            "Puebla", "Querétaro", "Quintana Roo", "San Luis Potosí", "Sinaloa",
            "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz",
            "Yucatán", "Zacatecas");

    private ControlDireccionesEnvio control;
    private boolean initialized = false;
    private Stage stage;
    private Scene sceneSeleccionar;
    private Scene sceneFormulario;

    // Estado local para rastrear la dirección activa seleccionada por el usuario.
    private DireccionEnvio direccionSeleccionada = null;

    // ===== Campos del FXML "Seleccionar Dirección" =====
    @FXML
    private FlowPane contenedorDirecciones;
    @FXML
    private Button botonContinuarPago;
    @FXML
    private Button botonRegresar;
    // ===== Campos del FXML "Formulario Dirección" =====
    @FXML
    private Label etiquetaError;
    @FXML
    private TextField campoNombreCompleto;
    @FXML
    private TextField campoCalle;
    @FXML
    private TextField campoNumero;
    @FXML
    private TextField campoColonia;
    @FXML
    private TextField campoCiudad;
    @FXML
    private ComboBox<String> comboEstado;
    @FXML
    private TextField campoCodigoPostal;

    public VistaDireccionesEnvio() {
    }

    public void setControlador(ControlDireccionesEnvio control) {
        this.control = control;
    }

    // Inicializa la interfaz gráfica cargando los archivos FXML.
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
            stage.setTitle("Direcciones de envío");

            // Carga de la escena principal (Lista de direcciones)
            FXMLLoader loaderSeleccionar = new FXMLLoader(
                    getClass().getResource("/fxml/ventana_SeleccionarDireccion.fxml"));
            loaderSeleccionar.setController(this);
            sceneSeleccionar = new Scene(loaderSeleccionar.load());

            // Carga de la escena secundaria (Formulario de registro)
            FXMLLoader loaderFormulario = new FXMLLoader(
                    getClass().getResource("/fxml/ventana_FormularioDireccion.fxml"));
            loaderFormulario.setController(this);
            sceneFormulario = new Scene(loaderFormulario.load());

            comboEstado.getItems().addAll(ESTADOS);

            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Renderiza la lista de direcciones asociadas al cliente en la interfaz.
    public void muestraDirecciones(List<DireccionEnvio> direcciones, Cliente cliente) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.muestraDirecciones(direcciones, cliente));
            return;
        }

        inicializarUI();

        // Asignamos la dirección predeterminada actual si el cliente tiene una.
        this.direccionSeleccionada = cliente.getDireccionPredeterminada();

        contenedorDirecciones.getChildren().clear();

        // Genera dinámicamente las tarjetas UI para cada dirección recuperada.
        for (DireccionEnvio direccion : direcciones) {
            boolean esPredeterminada = cliente.getDireccionPredeterminada() != null
                    && cliente.getDireccionPredeterminada().getIdDireccion() == direccion.getIdDireccion();
            contenedorDirecciones.getChildren().add(construirTarjetaDireccion(direccion, esPredeterminada));
        }
        
        // Agrega siempre al final la tarjeta para registrar una nueva dirección.
        contenedorDirecciones.getChildren().add(construirTarjetaNuevaDireccion());

        boolean hayDirecciones = !direcciones.isEmpty();
        botonContinuarPago.setVisible(hayDirecciones);
        botonContinuarPago.setManaged(hayDirecciones);

        // Bloquea el flujo hacia el pago si no hay una dirección explícitamente seleccionada.
        botonContinuarPago.setDisable(this.direccionSeleccionada == null);

        botonRegresar.setVisible(!hayDirecciones);
        botonRegresar.setManaged(!hayDirecciones);

        stage.setScene(sceneSeleccionar);
        stage.show();
    }

    // Construye el componente visual (Tarjeta) para mostrar los datos de una dirección.
    private VBox construirTarjetaDireccion(DireccionEnvio direccion, boolean esPredeterminada) {
        VBox tarjeta = new VBox(4);
        tarjeta.setPrefWidth(180);
        
        // Resaltado visual si la dirección es la predeterminada
        tarjeta.setStyle("-fx-border-color: " + (esPredeterminada ? "black" : "#dddddd")
                + "; -fx-border-width: " + (esPredeterminada ? "2" : "1") + "; -fx-padding: 12;");

        if (esPredeterminada) {
            Label tagPredeterminada = new Label("PREDETERMINADA");
            tagPredeterminada.setStyle("-fx-background-color: #eeeeee; -fx-padding: 2 6; -fx-font-size: 10px;");
            tarjeta.getChildren().add(tagPredeterminada);
        }

        Label nombre = new Label(direccion.getNombreCompleto());
        nombre.setStyle("-fx-font-weight: bold;");

        Label calleNumero = new Label(direccion.getCalle() + " " + direccion.getNumero() + ", " + direccion.getColonia());
        calleNumero.setWrapText(true);

        Label ciudadEstado = new Label(direccion.getCiudad() + ", " + direccion.getEstado() + " " + direccion.getCodigoPostal());
        ciudadEstado.setWrapText(true);
        ciudadEstado.setStyle("-fx-text-fill: #666666;");

        Button botonSeleccionar = new Button(esPredeterminada ? "Seleccionada" : "Seleccionar");
        botonSeleccionar.setDisable(esPredeterminada);
        botonSeleccionar.setMaxWidth(Double.MAX_VALUE);
        
        if (esPredeterminada) {
            botonSeleccionar.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        }
        
        // Acción al elegir esta tarjeta como dirección de envío
        botonSeleccionar.setOnAction(evento -> {
            this.direccionSeleccionada = direccion;
            botonContinuarPago.setDisable(false);

            if (control != null) {
                control.establecerPredeterminada(direccion.getIdDireccion());
            }
        });

        Button botonEliminar = new Button("Eliminar");
        botonEliminar.setStyle("-fx-text-fill: #c00000;");
        botonEliminar.setMaxWidth(Double.MAX_VALUE);
        botonEliminar.setOnAction(evento -> {
            if (control != null) {
                control.solicitarEliminarDireccion(direccion.getIdDireccion());
            }
        });

        tarjeta.getChildren().addAll(nombre, calleNumero, ciudadEstado, botonSeleccionar, botonEliminar);
        return tarjeta;
    }

    // Construye el componente visual especial para agregar una nueva dirección.
    private VBox construirTarjetaNuevaDireccion() {
        VBox tarjeta = new VBox(4);
        tarjeta.setPrefWidth(180);
        tarjeta.setPrefHeight(140);
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setStyle("-fx-border-color: #bbbbbb; -fx-border-style: dashed; -fx-padding: 12;");

        Label icono = new Label("⊕");
        icono.setStyle("-fx-font-size: 20px;");
        Label texto = new Label("Nueva Dirección");
        texto.setStyle("-fx-font-weight: bold;");
        Label subtexto = new Label("Añade una dirección de envío alternativa");
        subtexto.setWrapText(true);
        subtexto.setStyle("-fx-text-fill: #999999; -fx-font-size: 10px;");
        subtexto.setAlignment(Pos.CENTER);

        tarjeta.getChildren().addAll(icono, texto, subtexto);

        tarjeta.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent evento) {
                mostrarFormulario();
            }
        });

        return tarjeta;
    }

    // Transiciona la interfaz hacia la escena del formulario.
    public void mostrarFormulario() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::mostrarFormulario);
            return;
        }

        limpiarFormulario();
        stage.setScene(sceneFormulario);
    }

    // Restablece los campos de texto para asegurar que el formulario inicie en blanco.
    private void limpiarFormulario() {
        campoNombreCompleto.clear();
        campoCalle.clear();
        campoNumero.clear();
        campoColonia.clear();
        campoCiudad.clear();
        comboEstado.getSelectionModel().clearSelection();
        campoCodigoPostal.clear();
        ocultarError();
    }

    // Captura los datos del formulario, los empaca 
    @FXML
    private void handleGuardar() {
        if (!validarCamposObligatorios()) {
            return;
        }

        DatosDireccion datos = new DatosDireccion(
                campoNombreCompleto.getText().trim(),
                campoCalle.getText().trim(),
                campoNumero.getText().trim(),
                campoColonia.getText().trim(),
                campoCiudad.getText().trim(),
                comboEstado.getValue(),
                campoCodigoPostal.getText().trim());

        if (control != null) {
            control.procesarRegistroDireccion(datos);
        }
    }

    @FXML
    private void handleCancelar() {
        stage.setScene(sceneSeleccionar);
    }

    // Gestiona la transición hacia el módulo de pago (HU-03).
    @FXML
    private void handleContinuarPago() {
        // Validación de seguridad por si el botón fue habilitado erróneamente.
        if (direccionSeleccionada == null) {
            mostrarMensaje("Por favor selecciona una dirección antes de continuar.");
            return;
        }
        
        // Delega al controlador la apertura de la ventana de pago.
        if (control != null) {
            control.continuarAlPago(direccionSeleccionada);
        }
    }

    @FXML
    private void handleRegresar() {
        stage.close();
    }

    // Verifica que ningún campo crítico del formulario esté vacío.
    private boolean validarCamposObligatorios() {
        if (campoNombreCompleto.getText().trim().isEmpty()
                || campoCalle.getText().trim().isEmpty()
                || campoNumero.getText().trim().isEmpty()
                || campoColonia.getText().trim().isEmpty()
                || campoCiudad.getText().trim().isEmpty()
                || comboEstado.getValue() == null
                || campoCodigoPostal.getText().trim().isEmpty()) {
            mostrarError();
            return false;
        }
        ocultarError();
        return true;
    }

    private void mostrarError() {
        etiquetaError.setVisible(true);
        etiquetaError.setManaged(true);
    }

    private void ocultarError() {
        etiquetaError.setVisible(false);
        etiquetaError.setManaged(false);
    }

    // Muestra ventanas emergentes de información al usuario.
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