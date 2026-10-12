package mx.uam.ayd.proyecto.presentacion.hu03_bug_carrito.pago_exitoso;

import java.util.List;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.conffigPD.GestionEscena;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.carritoCompra;
import mx.uam.ayd.proyecto.presentacion.hu03_bug_carrito.peticion_pago.ControladorPago;

@Component 
public class VistaExitoPago {
    @FXML 
    private Text idPedido;
    @FXML 
    private Text idCarrito;
    @FXML 
    private Text fechaPedido;
    @FXML 
    private Text pagado;
    @FXML 
    private FlowPane articulos;



    private boolean inicializado;
    private Scene scene;
    private Scene escenaAnterior;
    private Stage stage;
    private ControladorExitoPago controladorExitoPago;
    private carritoCompra carritoCompra;

    // iniciamos iciando el escenario y posteriormente con las validaciones
    private void inicializarUI() {
        if (inicializado) {
            return;
        }

        // crea UI solo si estamos en el hilo de JAVAFX
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::inicializarUI);
            return;
        }

        try {

            // Cargamos el fxml que tiene que ver con esta ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana_pago_exitoso.fxml"));
            loader.setController(this); // le estamos diciendo a javafx que esta clase es la que controla el fxml
            scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/css/Pago.css").toExternalForm());

            inicializado = true;
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo cargar la ventana de pago.", e);
        }
    }

    // establecemos el controlador asiciado a esta ventana
    public void setControlador(ControladorExitoPago control) {
        this.controladorExitoPago= control;
    }

    public void muestra(carritoCompra car, Scene stg) {
        List<Producto> prod = car.getProductos();
        this.carritoCompra = car;
        this.stage = GestionEscena.getInstEscena().getStage();
        this.escenaAnterior = stg;

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestra(car, stg));
            return;
        }

        inicializarUI();

        stage.setScene(scene);
        stage.setTitle("Proceder con compra");

        //llenamos los id
        idPedido.setText("IdCompra:" + car.getCompra().getIdCompra());
        idCarrito.setText("IdCarrito:" + car.getIdCarrito());
        fechaPedido.setText("" + car.getCompra().getFecha());
        pagado.setText("$" + car.getTotalCalculado());
        // Borramos todo lo que este dentro de la ventana
        articulos.getChildren().clear();
        articulos.setOrientation(Orientation.HORIZONTAL);
        for (Producto p : prod) {
            HBox tarjeta = new HBox();
            tarjeta.getStyleClass().add("tarjetaProd");

            Label nombre = new Label(p.getnombre());
            nombre.getStyleClass().add("product-name");

            Label precio = new Label("$" + p.getPrecio());
            precio.getStyleClass().add("product-price");

            ImageView imagen = new ImageView(new Image(getClass().getResourceAsStream(p.getRutaImagen())));
            // Le damos un tamaño bonito a la imagen
            imagen.setFitHeight(140);
            imagen.setFitWidth(120);
            imagen.setPreserveRatio(true);
            tarjeta.getChildren().addAll(imagen, nombre, precio);

            // Aqui agregamos los productos en el contenedor
            articulos.getChildren().add(tarjeta);

        }
        // boxTodo.setSelected(true);
        stage.show();
    }

    @FXML
    public void handleVistaAnterior() {
        if (stage != null && escenaAnterior != null) {
            stage.setScene(escenaAnterior);
            stage.setTitle("Catálogo Mezicuil");
        }
    }
}
