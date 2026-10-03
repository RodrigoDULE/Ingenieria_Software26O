package mx.uam.ayd.proyecto.presentacion.HU_06;

import org.springframework.stereotype.Component;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra;

@Component
public class VistaVentanaExito {

    @FXML private Label lblNumeroOrden;
    @FXML private Label lblDestinatario;
    @FXML private Label lblFechaEntrega;
    @FXML private Label lblEstado;

    private ControladorVentanaExito controlador;
    private Stage stageActual; // Guardaremos la ventana que nos presten

    public VistaVentanaExito() {}

    public void setControlador(ControladorVentanaExito controlador) {
        this.controlador = controlador;
    }

    // Recibe el Stage existente en lugar de crear uno nuevo
    public void muestraExito(Stage ventanaActual, OrdenDeCompra orden, String nombreProveedor) 
    {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestraExito(ventanaActual, orden, nombreProveedor));
            return;
        }
        
        this.stageActual = ventanaActual; // Guardamos la referencia de la ventana

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana_EnvioExitosoDeOrden.fxml")); 
            loader.setController(this); 
            
            // Creamos solo el lienzo nuevo (Scene) y se lo pegamos a la ventana vieja (Stage)
            Scene nuevaEscena = new Scene(loader.load()); 
            stageActual.setScene(nuevaEscena);
            stageActual.setTitle("Orden de Compra Enviada con Éxito");
            
            // Inyectamos datos
            lblNumeroOrden.setText("PO-2023-" + orden.getId());
            lblDestinatario.setText(nombreProveedor);
            lblFechaEntrega.setText(String.valueOf(orden.getFechaRecepcion()));
            lblEstado.setText("Autorizada");
            
            stageActual.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cerrarVentana() {
        if (controlador != null) {
            // Le decimos al controlador que regrese, dándole la misma ventana
            controlador.volverAOrdenesPendientes(stageActual);
        }
    }
}