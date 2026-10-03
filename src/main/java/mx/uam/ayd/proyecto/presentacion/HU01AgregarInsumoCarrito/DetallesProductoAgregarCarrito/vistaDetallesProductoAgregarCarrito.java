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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.conffigPD.gestionCliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;

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
    
    private int contador = 1;
    private Producto actual;

    private Stage stage;
    private boolean inicializado = false;
    private controladorDetallesProductoAgregarCarrito controlDetalles;

    // constructor vacio
    public vistaDetallesProductoAgregarCarrito() {
    }

    // inicializamos el controlador con un setter
    public void setControlador(controladorDetallesProductoAgregarCarrito controlDetalles) {
        this.controlDetalles = controlDetalles;
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
            stage = new Stage();
            stage.setTitle("Detalles de Producto");

            // Cargamos el fxml que tiene que ver con esta ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-detalles-producto.fxml"));
            loader.setController(this); // le estamos diciendo a javafx que esta clase es la que controla el fxml
            Scene scene = new Scene(loader.load(), 600, 410);
            scene.getStylesheets().add(getClass().getResource("/css/estilos-mezicuil.css").toExternalForm()); //Estilo CSS
            stage.setScene(scene);

            inicializado = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void muestraDetallesProd(Producto producto) {
        contador = 1;
        actual = producto;

        System.out.println("EL producto en el que se esta trabajando "+actual);
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestraDetallesProd(actual));
            return;
        }

        inicializarUI();

        imagenDetalles.getChildren().clear();
        descripcionProdDetalles.getChildren().clear();
        nombreDetalles.getChildren().clear();
        nombreDetalles.setStyle("-fx-fill: black;"); //genero **IA**

        // inicializamos el panel con la informacion del producto
        nombreDetalles.getChildren().add(new Text(actual.getnombre()));
        precioDetalles.setText("$" + actual.getPrecio() + " MXN");
        descripcionProdDetalles.getChildren().add(new Text(actual.getDescripcion()));
        piezasDetalles.setText("Hay " + actual.getcantidadStock() + " disponibles dentro de stock. \nUsuario con id: " + gestionCliente.getInstance().getIdActivo());
        System.out.println("El numero de piezas disponibles dentro de stock es: " + actual.getcantidadStock());
        // Cargamos la ruta de la imagen
        ImageView imagen = new ImageView(new Image(getClass().getResourceAsStream(actual.getRutaImagen())));
        imagen.setFitWidth(300);
        imagen.setFitHeight(330);
        imagenDetalles.getChildren().add(imagen);
        cantidadTexto.setText(String.valueOf(contador));
        cantidadTexto.setDisable(true);

        stage.show();
    }

    //validacion de seleccion de cantidad correcta
    @FXML
    private void handleIncrementarDecrementar(ActionEvent event){
        Object obj = event.getSource();
        Button BotonPres = (Button) obj;
        
        //obtener el numero de piezas dentro de stock
        System.out.println("NUmero disponible "+ actual.getcantidadStock());
        int limite = actual.getcantidadStock();

        contador = (contador <= 0) ? 1 : (contador >= limite) ? (limite - 1) : contador;
        
        if("+".equals(BotonPres.getText())){
            contador ++;
        }else{
            contador --;
        }

        //Actualizamos el texto
        cantidadTexto.setText(String.valueOf(contador));
        System.out.println("Numero dentro de contador: " + contador);
    }

    @FXML
    private void handleAgregaraCarrito(){
        if(controlDetalles != null){
            System.out.println("Boton presionado");
            controlDetalles.agregarProductoaCarrito(actual, contador);
        }
    }

    @FXML 
    private void handleIrCarrito(){
        if(controlDetalles != null){
            controlDetalles.visitaCarritoCompra();
        }
    }
    
    //Metodo para mostrar mensajes flotantes
    public void mostrarMensaje(String mensaje){
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