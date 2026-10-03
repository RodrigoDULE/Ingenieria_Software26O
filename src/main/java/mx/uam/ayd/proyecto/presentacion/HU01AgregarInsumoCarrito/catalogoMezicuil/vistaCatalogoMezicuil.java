package mx.uam.ayd.proyecto.presentacion.HU01AgregarInsumoCarrito.catalogoMezicuil;

import java.util.List;

import org.springframework.stereotype.Component;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.conffigPD.gestionCliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;

/**
 * vistaCatalogoMezicuil
 */
@Component
public class vistaCatalogoMezicuil {

    private Stage stage;
    private boolean inicializado = false;
    private controladorCatalogoMezicuil controlCatalgo;

    @FXML
    private TextField buscaProducto;

    @FXML
    private RadioButton boxTodo;

    @FXML
    private FlowPane contenedorProductos;

    // inicializamos el metodo que permite visualzar la nueva ventana
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
            stage = new Stage();
            stage.setTitle("Catalogo Mezicuil");

            // Cargamos el fxml que tiene que ver con esta ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-catalogo-Mezicuil.fxml"));
            loader.setController(this); // le estamos diciendo a javafx que esta clase es la que controla el fxml
            Scene scene = new Scene(loader.load(), 650, 430);
            scene.getStylesheets().add(getClass().getResource("/css/estilos-mezicuil.css").toExternalForm());
            stage.setScene(scene);

            inicializado = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // establecemos el controlador asiciado a esta ventana
    public void setControlador(controladorCatalogoMezicuil control) {
        this.controlCatalgo = control;
    }

    public void muestra(List<Producto> prod) {

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestra(prod));
            return;
        }

        inicializarUI();
        // Borramos todo lo que este dentro de la ventana
        contenedorProductos.getChildren().clear();

        for (Producto p : prod) {
            VBox tarjeta = new VBox();
            tarjeta.getStyleClass().add("product-card");

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
            contenedorProductos.getChildren().add(tarjeta);

            // Le agregamos un escuchador a las tarjetas
            tarjeta.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent evento) {
                    handleProductoSeleccionado(p);
                }
            });
        }

        //Añadimos el boton del bot
        if (gestionCliente.getInstance().getIdActivo() == 6) {
            Button bot = new Button("Bot");
            //agregamos una tarjeta de bot
            VBox tarjeta = new VBox();
            tarjeta.getStyleClass().add("product-card");
            Label nombre = new Label("AsistenteBot");
            nombre.getStyleClass().add("product-name");
            tarjeta.getChildren().addAll(nombre, bot);
            contenedorProductos.getChildren().add(tarjeta);

            bot.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent evento) {
                  controlCatalgo.EnviarBot();
                }
            });
        }

        // boxTodo.setSelected(true);
        stage.show();
    }

    // validamos el criterio por el cual el cliente quiere el producto
    @FXML
    // Aqui le ponemos ActionEvent para ver que elemento dispara el evento
    private void handlebuscarCriterio(ActionEvent evento) {
        // 1. Preguntamos quién fue el componente que disparó este evento
        Object componenteOrigen = evento.getSource();

        // 2. Como sabemos que fue un RadioButton, lo "casteamos" (convertimos)
        RadioButton botonPresionado = (RadioButton) componenteOrigen;

        if (controlCatalgo != null) {
            /*
             * if ("Todo".equals(botonPresionado.getText())) {
             * System.out.println("Todo presionado");
             * controlCatalgo.validarCriterio("Todo");
             * } else {
             * System.out.println("Boton seleccionado" + botonPresionado.getText());
             * controlCatalgo.validarCriterio(botonPresionado.getText());
             * }
             */
            controlCatalgo.validarCriterio(botonPresionado.getText());
        } else {
            System.out.println("Error al inicalizar controlador");
        }

    }

    @FXML
    // Aqui le ponemos ActionEvent para ver que elemento dispara el evento
    private void handlebuscarCriterioBarra() {
        // verificacion de campo no
        if (buscaProducto.getText().isEmpty()) {
            mostrarMensaje("Ingresa el nombre de un producto");
            return;
        }

        if (controlCatalgo != null) {
            controlCatalgo.validarCriterio(buscaProducto.getText());
        }
    }

    // metodo para que ponamos acceder a las caracteristicas del producto
    // seleccionado
    private void handleProductoSeleccionado(Producto p) {
        System.out.println("dentro de handle");
        controlCatalgo.detallesProductoSeleccionado(p);
    }

    // visitar carrito desde la pantalla principal de la tienda
    @FXML
    private void handleIrCarrito() {
        if (controlCatalgo != null) {
            controlCatalgo.irCarrito();
        }
    }

    // Metodo para mostrar mensajes flotantes
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