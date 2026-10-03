package mx.uam.ayd.proyecto.presentacion.HU04FormularioMarketing;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import mx.uam.ayd.proyecto.negocio.EntidadNegocio.FormularioMarketing;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.FormularioMarketing.TipoContenido;
import mx.uam.ayd.proyecto.negocio.ServicioGeneracionContenido.VariacionContenido;

// Gestiona la interfaz gráfica, validación de campos del formulario  
// y la renderización dinámica de las variaciones generadas.
@Component
public class VistaFormularioMarketing {

    // Formato estándar requerido para la fecha de publicación
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yy");

    // Límite visual de tarjetas de variación en la pantalla de resultados
    private static final int MAXIMO_VARIACIONES = 3;

    private ControlFormularioMarketing control;
    private boolean initialized = false;
    private Stage stage;
    private Scene scenePrincipal;
    private Scene sceneResultados;
    private List<File> archivosSeleccionados = new ArrayList<>();

    // Elementos del formulario de captura (Pantalla Principal)
    @FXML private RadioButton radioSoloTexto;
    @FXML private RadioButton radioImagenEstatica;
    @FXML private CheckBox checkInstagramPost;
    @FXML private CheckBox checkFacebookPost;
    @FXML private CheckBox checkLinkedin;
    @FXML private CheckBox checkEmail;
    @FXML private Button botonSeleccionarArchivos;
    @FXML private TextField campoCantidadVariaciones;
    @FXML private TextField selectorFechaPublicacion;
    @FXML private Button botonIniciarGeneracion;

    // Elementos de la pantalla de Resultados
    @FXML private Label etiquetaResumenTipoContenido;
    @FXML private Label etiquetaResumenPlataformas;
    @FXML private Label etiquetaResumenFecha;
    @FXML private Label contenidoVariacion1;
    @FXML private Label contenidoVariacion2;
    @FXML private Label contenidoVariacion3;
    @FXML private Label descripcionVariacion1;
    @FXML private Label descripcionVariacion2;
    @FXML private Label descripcionVariacion3;
    @FXML private Button botonGenerarDeNuevo;

    // Botones dinámicos para seleccionar la variación ganadora
    @FXML private Button botonElegirVariacion1;
    @FXML private Button botonElegirVariacion2;
    @FXML private Button botonElegirVariacion3;

    public VistaFormularioMarketing() {
    }

    public void setControlador(ControlFormularioMarketing control) {
        this.control = control;
    }

    // Carga los archivos FXML y prepara las escenas de JavaFX.
    // Asegura que la inicialización ocurra en el hilo de la interfaz gráfica.
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
            stage.setTitle("Formulario de Marketing");

            FXMLLoader loaderPrincipal = new FXMLLoader(
                    getClass().getResource("/fxml/ventana_FormularioMarketingPrincipal.fxml"));
            loaderPrincipal.setController(this);
            scenePrincipal = new Scene(loaderPrincipal.load());

            FXMLLoader loaderResultados = new FXMLLoader(
                    getClass().getResource("/fxml/ventana_FormularioMarketingResultados.fxml"));
            loaderResultados.setController(this);
            sceneResultados = new Scene(loaderResultados.load());

            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Abre la ventana principal mostrando el formulario vacío
    public void muestra() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::muestra);
            return;
        }

        inicializarUI();
        stage.setScene(scenePrincipal);
        stage.show();
    }
    
    // Cierra la ventana (invocado por el controlador al cambiar de módulo)
    public void cerrarVentana() {
        if (stage != null) {
            stage.close();
        }
    }

    // Abre el explorador de archivos del sistema operativo para adjuntar referencias
    @FXML
    private void handleSeleccionarArchivos() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona los archivos de referencia");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Documentos", "*.pdf", "*.txt", "*.docx"));

        List<File> seleccionados = fileChooser.showOpenMultipleDialog(stage);
        if (seleccionados != null) {
            archivosSeleccionados = seleccionados;
        }
    }

    // Desencadena la validación del formulario y envía los datos al controlador
    @FXML
    private void handleIniciarGeneracion() {
        if (!validarFormulario()) {
            return;
        }

        if (control != null) {
            control.procesarGeneracion(
                    obtenerTipoContenido(),
                    obtenerPlataformasSeleccionadas(),
                    obtenerCantidadVariaciones(),
                    obtenerFechaPublicacion(),
                    archivosSeleccionados);
        }
    }

    // Regresa a la escena principal para permitir al usuario ajustar los datos
    @FXML
    private void handleGenerarDeNuevo() {
        stage.setScene(scenePrincipal);
    }

    // Verifica que todos los campos obligatorios cumplan con las reglas de negocio
    private boolean validarFormulario() {
        if (obtenerTipoContenido() == null) {
            mostrarMensaje("Selecciona un tipo de contenido.");
            return false;
        }
        if (obtenerPlataformasSeleccionadas().isEmpty()) {
            mostrarMensaje("Selecciona al menos una plataforma de destino.");
            return false;
        }
        Integer cantidad = obtenerCantidadVariaciones();
        if (cantidad == null || cantidad < 1 || cantidad > MAXIMO_VARIACIONES) {
            mostrarMensaje("Indica cuántas variaciones quieres generar (entre 1 y " + MAXIMO_VARIACIONES + ").");
            return false;
        }
        if (obtenerFechaPublicacion() == null) {
            mostrarMensaje("Indica una fecha de publicación válida, formato dd/mm/aa.");
            return false;
        }
        return true;
    }

    private TipoContenido obtenerTipoContenido() {
        if (radioSoloTexto.isSelected()) {
            return TipoContenido.SOLO_TEXTO;
        }
        if (radioImagenEstatica.isSelected()) {
            return TipoContenido.IMAGEN_ESTATICA;
        }
        return null;
    }

    private List<String> obtenerPlataformasSeleccionadas() {
        List<String> plataformas = new ArrayList<>();
        if (checkInstagramPost.isSelected()) plataformas.add("Instagram post");
        if (checkFacebookPost.isSelected()) plataformas.add("Facebook post");
        if (checkLinkedin.isSelected()) plataformas.add("Linkedin");
        if (checkEmail.isSelected()) plataformas.add("Email");
        return plataformas;
    }

    private Integer obtenerCantidadVariaciones() {
        try {
            return Integer.parseInt(campoCantidadVariaciones.getText().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Pasa el texto introducido a un objeto LocalDate manejable por la lógica de negocio
    private LocalDate obtenerFechaPublicacion() {
        try {
            return LocalDate.parse(selectorFechaPublicacion.getText().trim(), FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // Reactiva el botón principal tras finalizar un proceso
    public void habilitarBotonGenerar() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::habilitarBotonGenerar);
            return;
        }
        botonIniciarGeneracion.setDisable(false);
    }

    // Construye la vista de resultados dinámicamente según el tipo de contenido generado
    // y asigna eventos a los botones de selección
    public void mostrarListaVariaciones(FormularioMarketing formulario, List<VariacionContenido> variaciones) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.mostrarListaVariaciones(formulario, variaciones));
            return;
        }

        // Llenar el resumen del encabezado
        etiquetaResumenTipoContenido.setText(formulario.getTipoContenido().name());
        etiquetaResumenPlataformas.setText(String.join(", ", formulario.getPlataformasDestino()));
        etiquetaResumenFecha.setText(formulario.getFechaEstimadaPublicacion().toString());

        Label[] cajasContenido = { contenidoVariacion1, contenidoVariacion2, contenidoVariacion3 };
        Label[] cajasDescripcion = { descripcionVariacion1, descripcionVariacion2, descripcionVariacion3 };
        Button[] botonesElegir = { botonElegirVariacion1, botonElegirVariacion2, botonElegirVariacion3 };

        // Simulación de imágenes para las variaciones visuales
        List<String> rutasImagenes = Arrays.asList(
            "/Imagenes/ImagenContenido1.png",
            "/Imagenes/ImagenContenido2.png",
            "/Imagenes/ImagenContenido3.png"
        );
        Collections.shuffle(rutasImagenes);

        for (int i = 0; i < cajasContenido.length; i++) {
            // Reinicio visual de las tarjetas para evitar superposición de datos
            cajasContenido[i].setGraphic(null); 
            cajasContenido[i].setText("");
            cajasContenido[i].setStyle("");
            botonesElegir[i].setOnAction(null);

            if (i < variaciones.size()) {
                VariacionContenido variacion = variaciones.get(i);

                if (formulario.getTipoContenido() == TipoContenido.IMAGEN_ESTATICA) {
                    
                    // Renderizado de imagen
                    try {
                        Image imagen = new Image(getClass().getResourceAsStream(rutasImagenes.get(i)));
                        ImageView imageView = new ImageView(imagen);

                        imageView.setFitWidth(150); 
                        imageView.setFitHeight(160);
                        imageView.setPreserveRatio(true);

                        cajasContenido[i].setGraphic(imageView);
                    } catch (Exception e) {
                        cajasContenido[i].setText("Error visual");
                        System.err.println("No se encontró la imagen: " + rutasImagenes.get(i));
                    }

                } else {
                    
                    // Renderizado de texto
                    cajasContenido[i].setWrapText(true); 
                    cajasContenido[i].setText(variacion.getNombre());
                    cajasContenido[i].setStyle(
                        "-fx-alignment: center; " +
                        "-fx-text-alignment: center; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12px 15px 12px 15px;"
                    );
                }

                cajasDescripcion[i].setText(variacion.getDescripcion());

                // Configura el botón para enviar la variación seleccionada al controlador
                botonesElegir[i].setVisible(true);
                botonesElegir[i].setManaged(true);
                botonesElegir[i].setOnAction(evento -> {
                    if (control != null) {
                        control.seleccionarVariacion(variacion); 
                    }
                });

            } else {
                // Ocultar tarjetas no utilizadas si se solicitaron menos del máximo
                cajasContenido[i].setText("");
                cajasDescripcion[i].setText("");
                botonesElegir[i].setVisible(false);
                botonesElegir[i].setManaged(false);
            }
        }

        stage.setScene(sceneResultados);
    }

    // Despliega una alerta emergente en la interfaz
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