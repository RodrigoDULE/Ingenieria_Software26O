package mx.uam.ayd.proyecto.presentacion.HU01AgregarInsumoCarrito.DetallesProductoAgregarCarrito;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.conffigPD.GestionEscena;
import mx.uam.ayd.proyecto.conffigPD.gestionCliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Resena;

import java.util.List; 

/**
 * vistaDetallesProductoAgregarCarrito
 */
@Component
public class vistaDetallesProductoAgregarCarrito {

    @FXML
    private FlowPane imagenDetalles;
    @FXML
    private TextFlow nombreDetalles;
    @FXML
    private Text precioDetalles;
    @FXML
    private Text piezasDetalles;
    @FXML
    private TextFlow descripcionProdDetalles;
    @FXML
    private Button incrementarDetalles;
    @FXML
    private Button decrementarDetalles;
    @FXML
    private TextField cantidadTexto;

    // Componentes FXML para la sección de reseñas
    @FXML
    private TextField campoTituloResena;
    @FXML
    private TextArea campoComentarioResena;
    @FXML
    private ComboBox<Integer> comboCalificacion;
    @FXML
    private Button botonPublicarResena;
    @FXML 
    private VBox contenedorResenas; 

    private int contador = 1;
    private Producto actual;

    private Scene scene;
    private Scene escenaAnterior;
    private Stage stage;
    private boolean inicializado = false;
    private controladorDetallesProductoAgregarCarrito controlDetalles;

    // Constructor vacío
    public vistaDetallesProductoAgregarCarrito() {
    }

    // Inicializamos el controlador con un setter
    public void setControlador(controladorDetallesProductoAgregarCarrito controlDetalles) {
        this.controlDetalles = controlDetalles;
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
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/ventana-detalles-producto.fxml"));
            loader.setController(this);

            scene = new Scene(loader.load());
            scene.getStylesheets().add(
                    getClass().getResource("/css/estilos-mezicuil.css").toExternalForm());

            inicializado = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void muestraDetallesProd(Producto producto, Stage stg) {
        contador = 1;
        actual = producto;
        this.stage = stg;
        this.escenaAnterior = stg.getScene();

        // Cargar las reseñas del producto si el controlador está disponible
        if (controlDetalles != null) {
            List<Resena> resenas = controlDetalles.obtenerResenas(producto);
            mostrarResenasEnPantalla(resenas);
        }

        System.out.println("El producto en el que se está trabajando: " + actual);
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestraDetallesProd(actual, stage));
            return;
        }

        inicializarUI();

        stage.setScene(scene);
        stage.setTitle("Detalles de Producto");

        imagenDetalles.getChildren().clear();
        descripcionProdDetalles.getChildren().clear();
        nombreDetalles.getChildren().clear();
        nombreDetalles.setStyle("-fx-fill: black;");

        // Inicializamos el panel con la información del producto
        nombreDetalles.getChildren().add(new Text(actual.getnombre()));
        precioDetalles.setText("$" + actual.getPrecio() + " MXN");
        descripcionProdDetalles.getChildren().add(new Text(actual.getDescripcion()));
        piezasDetalles.setText("Hay " + actual.getcantidadStock() + " piezas disponibles dentro de stock");
        
        System.out.println("El número de piezas disponibles dentro de stock es: " + actual.getcantidadStock());
        
        // Cargamos la ruta de la imagen
        ImageView imagen = new ImageView(new Image(getClass().getResourceAsStream(actual.getRutaImagen())));
        imagen.setFitWidth(300);
        imagen.setFitHeight(330);
        imagenDetalles.getChildren().add(imagen);
        
        cantidadTexto.setText(String.valueOf(contador));
        cantidadTexto.setDisable(true);

        // Inicializar el ComboBox de calificación (1 a 5 estrellas)
        if (comboCalificacion != null) {
            comboCalificacion.getItems().clear();
            comboCalificacion.getItems().addAll(1, 2, 3, 4, 5);
            comboCalificacion.setValue(5); // Valor por defecto en 5 estrellas
        }

        // Limpiamos los campos de reseña cada vez que se abre un nuevo producto
        if (campoTituloResena != null) campoTituloResena.clear();
        if (campoComentarioResena != null) campoComentarioResena.clear();

        stage.show();
    }

    @FXML
    private void handleIncrementarDecrementar(ActionEvent event) {
        Object obj = event.getSource();
        Button BotonPres = (Button) obj;

        int limite = actual.getcantidadStock();
        contador = (contador <= 0) ? 1 : (contador >= limite) ? (limite - 1) : contador;

        if ("+".equals(BotonPres.getText())) {
            contador++;
        } else {
            contador--;
        }

        cantidadTexto.setText(String.valueOf(contador));
        System.out.println("Número dentro de contador: " + contador);
    }

    @FXML
    private void handleAgregaraCarrito() {
        if (controlDetalles != null) {
            System.out.println("Botón Agregar Carrito presionado");
            controlDetalles.agregarProductoaCarrito(actual, contador);
        }
    }

    @FXML
    private void handleIrCarrito() {
        if (controlDetalles != null) {
            controlDetalles.visitaCarritoCompra();
        }
    }

    @FXML 
    private void handlePublicarResena() {
        if (controlDetalles != null) {
            try {
                int calificacion = comboCalificacion.getValue() != null ? comboCalificacion.getValue() : 5;
                String titulo = campoTituloResena.getText();
                String comentario = campoComentarioResena.getText();

                controlDetalles.publicarResena(actual, calificacion, titulo, comentario);
                
                // Limpiamos los campos después de publicar con éxito
                campoTituloResena.clear();
                campoComentarioResena.clear();
                comboCalificacion.setValue(5);

            } catch (Exception e) {
                mostrarMensaje("Error al recolectar los datos de la reseña: " + e.getMessage());
            }
        }
    }

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

    @FXML
    public void handleVistaAnterior() {
        if (stage != null && escenaAnterior != null) {
            stage.setScene(escenaAnterior);
            stage.setTitle("Catálogo Mezicuil");
        }
    }

    // Método para mostrar las reseñas en la interfaz gráfica
    public void mostrarResenasEnPantalla(List<Resena> listaResenas) {
        if (contenedorResenas == null) {
            return; 
        }
        contenedorResenas.getChildren().clear();
        
        if (listaResenas == null || listaResenas.isEmpty()) {
            Text mensajeVacio = new Text("Aún no hay reseñas para este producto. ¡Sé el primero en opinar!");
            mensajeVacio.setStyle("-fx-fill: #7f8c8d; -fx-font-style: italic;");
            contenedorResenas.getChildren().add(mensajeVacio);
            return;
        }

        // Iteramos sobre cada reseña guardada para dibujarla en pantalla
        for (Resena r : listaResenas) {
            VBox tarjeta = new VBox(4);
            tarjeta.setStyle("-fx-background-color: #f9f9f9; -fx-padding: 10; -fx-background-radius: 6; -fx-border-color: #e0e0e0; -fx-border-radius: 6;");

            int estrellasCount = r.getCalificacion();
            String estrellasStr = "★".repeat(Math.max(0, estrellasCount)) + "☆".repeat(Math.max(0, 5 - estrellasCount));
        
            javafx.scene.text.Text lblEstrellas = new javafx.scene.text.Text(estrellasStr);
            lblEstrellas.setStyle("-fx-fill: #f39c12; -fx-font-size: 14px; -fx-font-weight: bold;");

            javafx.scene.text.Text lblTitulo = new javafx.scene.text.Text(r.getTitulo());
            lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

            javafx.scene.text.Text lblComentario = new javafx.scene.text.Text(r.getComentario());
            lblComentario.setStyle("-fx-font-size: 12px;");
            lblComentario.setWrappingWidth(500);

            tarjeta.getChildren().addAll(lblEstrellas, lblTitulo, lblComentario);
            contenedorResenas.getChildren().add(tarjeta);
        }
    }
}


