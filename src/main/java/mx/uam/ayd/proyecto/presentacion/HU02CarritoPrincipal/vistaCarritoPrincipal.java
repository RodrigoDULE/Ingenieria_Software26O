package mx.uam.ayd.proyecto.presentacion.HU02CarritoPrincipal;

import java.util.List;

import org.springframework.stereotype.Component;
//librerias para escuchador
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import mx.uam.ayd.proyecto.conffigPD.GestionEscena;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.carritoCompra;

@Component
public class vistaCarritoPrincipal {

    // Añadimos todos los atributos que estan dentro del archivo FXM que cambiaran
    // su valor
    @FXML
    private Text cantidadTotalcarrito;
    @FXML
    private Text costoEnvioCarrito;
    @FXML
    private Text costoTotal;
    @FXML
    private FlowPane contenedorProductoscarrito;

    private Scene scene;
    private Scene escenaAnterior;
    private Stage stage;
    private boolean inicializado = false;
    private controladorCarritoPrincipal controlcarritoPrincipal;
    private carritoCompra carritoCompra;

    // constructor vacio
    public vistaCarritoPrincipal() {
    }

    // inicializamos el controlador con un setter
    public void setControlador(controladorCarritoPrincipal carritoPrincipal) {
        this.controlcarritoPrincipal = carritoPrincipal;
    }

    // inicializamos UI
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-carrito-principal.fxml"));
            loader.setController(this); // le estamos diciendo a javafx que esta clase es la que controla el fxml
            scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/css/estilos-mezicuil.css").toExternalForm());// Estilo
                                                                                                             // css
            // stage.setScene(scene);

            inicializado = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void muestraCarrito(carritoCompra carrito, Stage stg) {
        this.carritoCompra = carrito;
        this.stage = stg;
        this.escenaAnterior = stg.getScene();

        System.out.println("Ventana carrito mostrada");
        List<Producto> listaProd = carrito.getProductoenCarrito();

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestraCarrito(carrito, stage));
            return;
        }

        inicializarUI();
        // borramos todo lo que ente dentro de la ventana

        stage.setScene(scene);
        stage.setTitle("Carrito Compras");

        contenedorProductoscarrito.getChildren().clear();
        contenedorProductoscarrito.setOrientation(Orientation.VERTICAL);

        cantidadTotalcarrito.setText("$" + carrito.getTotalCalculado() + "MXN");

        if (carrito.getenvioGratis()) {
            costoEnvioCarrito.setText("Envio Gratis");
            costoTotal.setText("" + carrito.getTotalCalculado() + "MXN");
        } else {
            costoEnvioCarrito.setText("$320 MXN");
            costoTotal.setText("" + (carrito.getTotalCalculado() + 320) + "MXN");
            carrito.setTotalCalculado(carrito.getTotalCalculado() + 320); // Le añadimos el costo de envio
        }

        // contenedor siempre va a estar vacio, por eso es mejor si la lista esta vacia
        if (listaProd.isEmpty()) {
            // genero **IA**
            VBox emptyCartCard = new VBox();
            emptyCartCard.getStyleClass().add("empty-cart-container");

            Text icon = new Text("\uD83D\uDED1");
            icon.getStyleClass().add("empty-cart-icon");
            icon.setStyle("-fx-font-size: 48px;");

            Text title = new Text("Tu carrito está vacío");
            title.getStyleClass().add("empty-cart-title");

            Text description = new Text(
                    "Explora nuestros productos disponibles\ny agrega lo que te guste a tu carrito.");
            description.getStyleClass().add("empty-cart-description");
            description.setStyle("-fx-text-alignment: center;");
            description.setWrappingWidth(250);

            emptyCartCard.getChildren().addAll(icon, title, description);
            contenedorProductoscarrito.getChildren().add(emptyCartCard);
            // genero **IA**
        } else {
            for (Producto dentro : listaProd) {
                VBox tarjeta = new VBox();
                tarjeta.getStyleClass().add("product-card");// Estilo css

                Label nombre = new Label(dentro.getnombre());
                nombre.getStyleClass().add("product-name");// Estilo css

                Button eliminarProd = new Button("Eliminar");
                eliminarProd.getStyleClass().add("primary-button");// Estilo css
                Label precioUnitario = new Label("$" + dentro.getPrecio() + "MXN");
                precioUnitario.getStyleClass().add("product-price");// Estilo css

                ImageView imagenProd = new ImageView(new Image(getClass().getResourceAsStream(dentro.getRutaImagen())));
                imagenProd.setFitHeight(150);
                imagenProd.setFitWidth(130);
                tarjeta.getChildren().addAll(imagenProd, nombre, precioUnitario, eliminarProd);

                // Aqui agregamos los productos en el contenedor
                contenedorProductoscarrito.getChildren().add(tarjeta);

                // Le agregamos un escuchador a las tarjetas
                eliminarProd.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent evento) {
                        handleEliminarProd(dentro);
                        contenedorProductoscarrito.getChildren().remove(tarjeta); // Eliminamos la tarjeta del producto
                    }
                });
            }
        }

        //GestionEscena.getInstEscena().iniciarEscena(stg); // guarda esta escena

        stage.show();
    }

    private void handleEliminarProd(Producto p) {
        if (controlcarritoPrincipal != null) {
            controlcarritoPrincipal.EliminarProd(p);
        }
    }

    @FXML
    private void handleDireccionEnvio() {
        if (controlcarritoPrincipal != null) {
            if (carritoCompra.getProductos().isEmpty()) {
                System.out.println("El carrito esta vacio, lo sentimos");
                return;
            }
            controlcarritoPrincipal.irDireccionEnvio();
            stage.close();
        }

    }

    @FXML
    public void handleVistaAnterior() {
        if (stage != null && escenaAnterior != null) {
            stage.setScene(escenaAnterior);
            stage.setTitle("Catálogo Mezicuil");
        }
    }

    // Metodo para mostrar mensajes de error o advertencia
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
