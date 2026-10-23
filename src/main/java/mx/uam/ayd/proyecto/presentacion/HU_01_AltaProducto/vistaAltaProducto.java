package mx.uam.ayd.proyecto.presentacion.HU_01_AltaProducto;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

@Component
public class vistaAltaProducto {

    private ControladorAltaProducto control;
    private boolean inicializado = false;
    private Stage stage;

    // Archivo físico elegido por el usuario en su PC (se guarda hasta presionar Guardar)
    private File archivoImagenSeleccionado;

    // ==========================================
    // Componentes FXML de la Interfaz
    // ==========================================
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtStock;
    @FXML
    private TextField txtContenidoMCL;
    @FXML
    private TextField txtGradoAlcohol;
    @FXML
    private ComboBox<String> cmbCategoria;
    @FXML
    private TextField txtImagen;
   
    @FXML
    private TextField txtTipoAgave;
    @FXML
    private TextField txtRegion;

    @FXML
    private Button btnSeleccionarImagen;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    public vistaAltaProducto() {
    }

    public void setControlador(ControladorAltaProducto control) {
        this.control = control;
    }

    private void inicializarUI() {
        if (inicializado) {
            return;
        }

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::inicializarUI);
            return;
        }

        try {
            stage = new Stage();
            stage.setTitle("Alta de Productos - Tienda Mezicuil");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-alta-producto.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 600, 650);
            stage.setScene(scene);

            cmbCategoria.getItems().addAll("Mezcales", "Complementos");

            // Escenario 5: Si cierra la ventana con la 'X', limpiamos los datos en memoria
            stage.setOnCloseRequest(event -> limpiarCampos());

            inicializado = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void muestra() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::muestra);
            return;
        }

        inicializarUI();
        limpiarCampos();
        btnGuardar.setDisable(true);
        stage.show();
    }

    /**
     * Abre el explorador de archivos nativo para elegir una imagen de la PC.
     */
    @FXML
    private void handleSeleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Fotografía del Producto");

        // Filtramos para que el administrador solo pueda elegir archivos de imagen válidos
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Imágenes (PNG, JPG, JPEG)", "*.png", "*.jpg", "*.jpeg")
        );

        File archivoElegido = fileChooser.showOpenDialog(stage);

        if (archivoElegido != null) {
            this.archivoImagenSeleccionado = archivoElegido;
            // Reemplazamos espacios por guiones bajos en el nombre destino y armamos la ruta del catálogo
            String nombreLimpio = archivoElegido.getName().replaceAll("\\s+", "_");
            txtImagen.setText("/Imagenes/" + nombreLimpio);
            verificarCampos();
        }
    }

    /**
     * Escenario 2: Verifica en tiempo real si todos los campos obligatorios y la foto están listos.
     */
    @FXML
    public void verificarCampos() {
        boolean camposVacios = txtNombre.getText().trim().isEmpty()
                || txtPrecio.getText().trim().isEmpty()
                || txtDescripcion.getText().trim().isEmpty()
                || txtStock.getText().trim().isEmpty()
                || txtContenidoMCL.getText().trim().isEmpty()
                || txtGradoAlcohol.getText().trim().isEmpty()
                || cmbCategoria.getValue() == null
                || txtImagen.getText().trim().isEmpty()
                || archivoImagenSeleccionado == null
                || txtTipoAgave.getText().trim().isEmpty()
                || txtRegion.getText().trim().isEmpty();

        btnGuardar.setDisable(camposVacios);
    }

    @FXML
    private void handleGuardarProducto() {
        try {
            String nombre = txtNombre.getText();
            float precio = Float.parseFloat(txtPrecio.getText().trim());
            String descripcion = txtDescripcion.getText();
            int stock = Integer.parseInt(txtStock.getText().trim());
            int contenido = Integer.parseInt(txtContenidoMCL.getText().trim());
            float grados = Float.parseFloat(txtGradoAlcohol.getText().trim());
            String categoria = cmbCategoria.getValue();
            String rutaRelativaImagen = txtImagen.getText();
           
            String agave = txtTipoAgave.getText();
            String region = txtRegion.getText();

            if (control != null) {
                control.guardarProducto(nombre, precio, descripcion, stock, contenido,
                        grados, categoria, rutaRelativaImagen,  agave, region,
                        archivoImagenSeleccionado);
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("Error de formato", "Verifique que el precio, stock, contenido, volumen y grados de alcohol sean números válidos.", AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancelar() {
        limpiarCampos();
        if (stage != null) {
            stage.close();
        }
    }

    public void limpiarCampos() {
        if (!inicializado) return;
        txtNombre.clear();
        txtPrecio.clear();
        txtDescripcion.clear();
        txtStock.clear();
        txtContenidoMCL.clear();
        txtGradoAlcohol.clear();
        cmbCategoria.getSelectionModel().clearSelection();
        txtImagen.clear();
        archivoImagenSeleccionado = null;
        txtTipoAgave.clear();
        txtRegion.clear();
        btnGuardar.setDisable(true);
    }

    public void mostrarMensaje(String titulo, String mensaje, AlertType tipo) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.mostrarMensaje(titulo, mensaje, tipo));
            return;
        }

        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}