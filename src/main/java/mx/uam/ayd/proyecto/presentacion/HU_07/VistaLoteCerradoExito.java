package mx.uam.ayd.proyecto.presentacion.HU_07;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra;

@Component
public class VistaLoteCerradoExito {

    @FXML private Label lblNumeroOrden;
    @FXML private Label lblEstado;
    @FXML private Label lblFechaRecepcion;

    private ControladorLoteCerradoExito controlador;
    private Stage stageActual;

    public VistaLoteCerradoExito(){}

    public void setControlador(ControladorLoteCerradoExito controlador)
    {
        this.controlador = controlador;
    }

    public void muestraLoteCerrado(OrdenDeCompra orden)
    {

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestraLoteCerrado(orden));
            return;
        }

        this.stageActual = new Stage();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana_LoteCerradoExito.fxml")); 
            loader.setController(this); 
            
            Scene nuevaEscena = new Scene(loader.load()); 
            stageActual.setScene(nuevaEscena);
            stageActual.setTitle("Lote Cerrado con Éxito");
            
            // Inyectamos datos
            lblNumeroOrden.setText(orden.getFactura());
            lblFechaRecepcion.setText(String.valueOf(orden.getFechaRecepcion()));
            lblEstado.setText("RECIBIDA");
            
            stageActual.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cerrarVentana()
    {
        stageActual.close();
    }
    

}