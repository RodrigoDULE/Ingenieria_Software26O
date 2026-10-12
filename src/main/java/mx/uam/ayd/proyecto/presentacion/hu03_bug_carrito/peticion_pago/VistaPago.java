package mx.uam.ayd.proyecto.presentacion.hu03_bug_carrito.peticion_pago;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.carritoCompra;

@Component
public class VistaPago {

    @FXML
    private TextField noTarjeta;
    @FXML
    private TextField nombreTitular;
    @FXML
    private TextField expiracion;
    @FXML
    private TextField cvv;
    @FXML
    private Button botonPagar;
    @FXML
    private FlowPane articulos;
    @FXML
    private Text subTotal;
    @FXML
    private Text total;
    @FXML
    private Text envio;

    private boolean inicializado;
    private Scene scene;
    private Scene escenaAnterior;
    private Stage stage;
    private ControladorPago controladorPago;
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana_pago.fxml"));
            loader.setController(this); // le estamos diciendo a javafx que esta clase es la que controla el fxml
            scene = new Scene(loader.load());
            expiracion.setTextFormatter(new TextFormatter<String>(change ->
                    change.getControlNewText().matches("(?:[01]?|(?:0[1-9]|1[0-2])(?:/\\d{0,2})?)")
                            ? change
                            : null));
            scene.getStylesheets().add(getClass().getResource("/css/Pago.css").toExternalForm());

            inicializado = true;
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo cargar la ventana de pago.", e);
        }
    }

    // establecemos el controlador asiciado a esta ventana
    public void setControlador(ControladorPago control) {
        this.controladorPago = control;
    }

    public void muestra(carritoCompra car, Stage stg) {
        List<Producto> prod = car.getProductos();
        this.carritoCompra = car;
        this.stage = stg;
        this.escenaAnterior = stg.getScene();

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestra(car, stg));
            return;
        }

        inicializarUI();

        stage.setScene(scene);
        stage.setTitle("Proceder con compra");

        subTotal.setText("" + car.getTotalCalculado() + "MXN");

        if (car.getenvioGratis()) {
            envio.setText("Envio Gratis");
            total.setText("" + car.getTotalCalculado() + "MXN");
        } else {
            envio.setText("320 MXN");
            total.setText("" + (car.getTotalCalculado() + 320) + "MXN");
            car.setTotalCalculado(car.getTotalCalculado() + 320); // Le añadimos el costo de envio
        }

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

    private boolean checkeoCampos(){
     if(noTarjeta.getText().isEmpty() || nombreTitular.getText().isEmpty() || expiracion.getText().isEmpty() || cvv.getText().isEmpty()){
        return false;
     }
     
     if(noTarjeta.getText().length() < 16 || cvv.getText().length() < 3 || noTarjeta.getText().length() > 16 || cvv.getText().length() > 3){
        return false;
     }
     
     return true;
    }

    @FXML 
    public void handlePagar(){
        if(controladorPago == null){
            return;
        }

        if(checkeoCampos()){
               if (!fechaExpiracionValida()) {
                   mostrarMensaje("La fecha de expiración debe tener formato MM/AA y no estar vencida.");
                   return;
               }
               controladorPago.pagarCarritoCompra(carritoCompra);
           }else{
               mostrarMensaje("Alguno de los campos no esta completo, o hacen falta digitos en tarjeta o cvv");
           }

    }

    private boolean fechaExpiracionValida() {
           String valor = expiracion.getText();
           if (!valor.matches("(0[1-9]|1[0-2])/\\d{2}")) {
               return false;
           }

           int mes = Integer.parseInt(valor.substring(0, 2));
           int anio = 2000 + Integer.parseInt(valor.substring(3, 5));
           YearMonth expiracionTarjeta = YearMonth.of(anio, mes);
           return !expiracionTarjeta.atEndOfMonth().isBefore(LocalDate.now());
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
}
