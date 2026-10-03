package mx.uam.ayd.proyecto.presentacion.HU_06;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;

import jakarta.annotation.PostConstruct;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.ServicioOrdenDeCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra;


@Component
public class ControladorConfirmarAutorizacion {

    private final VistaConfirmarAutorizacion vistaConfirmar;
    private final ControladorOrdenesPendientes controladorOrdenes;
    private final ControladorVentanaExito controladorExito;
    private final ServicioOrdenDeCompra servicioOrden;

    @Autowired
    public ControladorConfirmarAutorizacion(@Lazy ControladorOrdenesPendientes controladorOrdenes, 
        ControladorVentanaExito controladorExito, VistaConfirmarAutorizacion vistaConfirmar, ServicioOrdenDeCompra servicioOrden)
    {
        this.vistaConfirmar = vistaConfirmar;
        this.controladorOrdenes = controladorOrdenes;
        this.controladorExito = controladorExito;
        this.servicioOrden = servicioOrden;
    }

    @PostConstruct
    private void inicializarControlador()
    {
        vistaConfirmar.setControlador(this);
    }

    public void iniciaVentanaConfirmacion(Stage ventanaActual, OrdenDeCompra orden, String nombreProveedor)
    {
        vistaConfirmar.muestraConfirmar(ventanaActual, orden, nombreProveedor);
    }

    public void volverAOrdenesPendientes(Stage ventanaActual)
    {
        controladorOrdenes.iniciaVentanaOrdenesCreadas();
        ventanaActual.close();
    }

    public void  irAVentanaExito(Stage ventanaActual, OrdenDeCompra orden, String nombreProveedor)
    {
        OrdenDeCompra ordenActualizada = servicioOrden.autorizarOrden(orden.getId());
        if(ordenActualizada != null)
        {
            controladorExito.iniciaVentanaExito(ventanaActual, orden, nombreProveedor);
        }else{
            System.out.println("La orden no se pudo autorizar.");
        }
    }

}