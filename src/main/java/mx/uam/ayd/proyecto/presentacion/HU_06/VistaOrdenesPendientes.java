package mx.uam.ayd.proyecto.presentacion.HU_06;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.util.List;

import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra;

@Component
public class VistaOrdenesPendientes {

    private ControladorOrdenesPendientes controlador;
    private Stage stageActual;

    @FXML private VBox contenedorTarjetas;

    public VistaOrdenesPendientes() {}

    public void setControlador(ControladorOrdenesPendientes controlador) {
        this.controlador = controlador;
    }

    public void crearWidgetsOrden(List<OrdenDeCompra> ordenes) 
    {
        contenedorTarjetas.getChildren().clear(); // limpiamos la lista visual

        if (ordenes == null || ordenes.isEmpty()) {
            Label lblVacio = new Label("No hay órdenes pendientes de revisión en este momento.");
            lblVacio.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d; -fx-font-style: italic; -fx-padding: 20;");
            
            // Lo agregamos a nuestro contenedor
            contenedorTarjetas.getChildren().add(lblVacio);
            return; // Detenemos la ejecución del método aquí
        }

        for (OrdenDeCompra orden : ordenes) {
            
          
            String nombreProveedor = "Proveedor Desconocido";
            if (orden.getDetalles() != null && !orden.getDetalles().isEmpty()) 
            {
                nombreProveedor = orden.getDetalles().get(0).getInsumo().getProveedor();
            }
            
            // aqui crearemos la tarjeta de cada orden
            VBox tarjeta = new VBox();
            tarjeta.setSpacing(10);
            tarjeta.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-color: #cccccc; -fx-border-radius: 5;");
            
            Label lblId = new Label("ID Orden: PO-2026-" + orden.getId());
            lblId.setStyle("-fx-font-weight: bold;");
            
            
            Label lblProveedor = new Label(nombreProveedor); 
            
            Label lblTotal = new Label("Total: $" + orden.getTotal() + " MXN");
            
            Button btnRevisar = new Button("Revisar Detalle");
            
            // Necesitamos guardar el valor en una variable 'final' para que el botón lo pueda usar adentro
            final String proveedorParaBoton = nombreProveedor;
            
            // Cada botón recuerda su propia orden
            btnRevisar.setOnAction(event -> {
                if(controlador != null) {
                    controlador.irADetalleOrden(stageActual, orden, proveedorParaBoton);
                }
            });

            // Agregamos todo a la tarjeta (incluyendo el label del proveedor opcional)
            tarjeta.getChildren().addAll(lblId, lblProveedor, lblTotal, btnRevisar);
            contenedorTarjetas.getChildren().add(tarjeta);
        }
    }


    public void muestraPendientes(List<OrdenDeCompra> ordenesPendientes) {
        if(!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestraPendientes(ordenesPendientes));
            return;
        }

        // Creamos la ventana desde cero
        this.stageActual = new Stage();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana_OrdenesCreadas.fxml"));
            loader.setController(this);

            Scene nuevaEscena = new Scene(loader.load());
            stageActual.setScene(nuevaEscena);
            stageActual.setTitle("Órdenes Generadas");

            // La lista ya la recibimos en los parámetros, solo la mandamos a dibujar
            crearWidgetsOrden(ordenesPendientes);

            stageActual.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cerrarVentana() {
        if(controlador != null) {
            stageActual.close();
        }
    }
}