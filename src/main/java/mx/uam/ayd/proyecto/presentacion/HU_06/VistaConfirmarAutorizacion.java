package mx.uam.ayd.proyecto.presentacion.HU_06;

import org.springframework.stereotype.Component;
import javafx.application.Platform;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;



@Component
public class VistaConfirmarAutorizacion{

    private ControladorConfirmarAutorizacion controlador;
    private Stage stageActual;

    private OrdenDeCompra ordenActual;
    private String proveedorActual;



    public VistaConfirmarAutorizacion(){}

    public void setControlador(ControladorConfirmarAutorizacion controlador)
    {
        this.controlador = controlador;
    }


    public void muestraConfirmar(Stage ventanaActual, OrdenDeCompra orden, String nombreProveedor)
    {
        if(!Platform.isFxApplicationThread())
        {
            Platform.runLater(() -> muestraConfirmar(ventanaActual, orden, nombreProveedor));
            return;
        }

        this.stageActual = ventanaActual;
        this.ordenActual = orden;
        this.proveedorActual = nombreProveedor;

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana_ConfirmarAutorizacionEnvio.fxml"));
            loader.setController(this);

            Scene nuevaEscena = new Scene(loader.load());
            stageActual.setScene(nuevaEscena);
            stageActual.setTitle("Confirmar Autorizacion");

            stageActual.show();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void cancelar()
    {
        if(controlador != null)
        {
            controlador.volverAOrdenesPendientes(stageActual);
        }
    }

    @FXML
    public void confirmar()
    {
        if(controlador != null){
            controlador.irAVentanaExito(stageActual, ordenActual, proveedorActual);
        }
    }


}