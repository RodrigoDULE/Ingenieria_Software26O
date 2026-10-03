package mx.uam.ayd.proyecto.presentacion.HU_06;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javafx.stage.Stage;
import java.util.List;

import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra;
import mx.uam.ayd.proyecto.negocio.ServicioOrdenDeCompra;



@Component
public class ControladorOrdenesPendientes{

    private final VistaOrdenesPendientes vistaPendientes;
    private final ControladorDetalleOrden controladorDetalle;
    private final ServicioOrdenDeCompra servicioOrden;

    @Autowired
    public ControladorOrdenesPendientes(VistaOrdenesPendientes vistaPendientes,
        ControladorDetalleOrden controladorDetalle, ServicioOrdenDeCompra servicioOrden)
    {
        this.vistaPendientes = vistaPendientes;
        this.controladorDetalle = controladorDetalle;
        this.servicioOrden = servicioOrden;
    }

    @PostConstruct
    private void inicializarControlador()
    {
        vistaPendientes.setControlador(this);
    }

    public void iniciaVentanaOrdenesCreadas()
    {
        List<OrdenDeCompra> ordenes = servicioOrden.obtenerOrdenesPendientes();
        vistaPendientes.muestraPendientes(ordenes);
    }

    public void irADetalleOrden(Stage ventanaActual, OrdenDeCompra orden, String nombreProveedor)
    {
        controladorDetalle.iniciaVentanaDetalle(ventanaActual, orden, nombreProveedor);
    }

}
