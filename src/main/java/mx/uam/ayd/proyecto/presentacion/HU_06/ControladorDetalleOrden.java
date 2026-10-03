package mx.uam.ayd.proyecto.presentacion.HU_06;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;

import jakarta.annotation.PostConstruct;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra;


@Component
public class ControladorDetalleOrden{

    private final VistaDetalleDeOrden vistaDetalle;
    private final ControladorConfirmarAutorizacion controladorConfirmar;
    private final ControladorOrdenesPendientes controladorOrdenes;

    @Autowired
    public ControladorDetalleOrden(ControladorConfirmarAutorizacion controladorConfirmar, 
        @Lazy ControladorOrdenesPendientes controladorOrdenes, VistaDetalleDeOrden vistaDetalle)
    {
        this.vistaDetalle = vistaDetalle;
        this.controladorConfirmar = controladorConfirmar;
        this.controladorOrdenes = controladorOrdenes;
    }

    @PostConstruct
    private void inicializarControlador()
    {
        vistaDetalle.setControlador(this);
    }

    public void iniciaVentanaDetalle(Stage ventanaActual, OrdenDeCompra orden, String nombreProveedor)
    {
        vistaDetalle.muestraDetalle(ventanaActual, orden, nombreProveedor);
    }

    public void volverAOrdenesPendientes(Stage ventanaActual)
    {
        controladorOrdenes.iniciaVentanaOrdenesCreadas();
        ventanaActual.close();
    }

    public void irAConfirmarAutorizacion(Stage ventanaActual, OrdenDeCompra orden, String proveedor)
    {
        controladorConfirmar.iniciaVentanaConfirmacion(ventanaActual, orden, proveedor);
    }


}