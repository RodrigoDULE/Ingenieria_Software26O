package mx.uam.ayd.proyecto.presentacion.HU_07;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.ServicioOrdenDeCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.DetalleOrden;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra;

@Component
public class ControladorEscaner {

    private final VistaEscaner vista;
    private final ServicioOrdenDeCompra servicioOrden;
    private final ControladorLoteCerradoExito controladorLote;

    @Autowired
    public ControladorEscaner(VistaEscaner vista, ServicioOrdenDeCompra servicioOrden,
        ControladorLoteCerradoExito controladorLote)
    {
        this.vista = vista;
        this.servicioOrden = servicioOrden;
        this.controladorLote = controladorLote;
    }

    @PostConstruct
    private void inicializarControlador()
    {
        vista.setControlador(this);
    }

    public void iniciaVentanaEscaner(OrdenDeCompra orden)
    {
        vista.muestraEscaner(orden);
    }

    public boolean procesarEscaneo(String skuEscaneado, OrdenDeCompra orden) {
        // Buscamos el producto en la lista de la orden
        for (DetalleOrden detalle : orden.getDetalles()) {
            
            if (detalle.getInsumo().getSku().equalsIgnoreCase(skuEscaneado)) {
                // Si lo encuentra y aún falta por escanear, suma 1
                if (detalle.getCantidadEscaneada() < detalle.getCantidad()) {
                    detalle.setCantidadEscaneada(detalle.getCantidadEscaneada() + 1);
                    return true; // Escaneo válido y registrado
                }
            }
        }
        return false; // No es de esta orden o ya se escaneó todo
    }

    public boolean verificarOrdenCompleta(OrdenDeCompra orden) {
        // Revisa si todas las cantidades escaneadas ya alcanzaron a las esperadas
        for (DetalleOrden detalle : orden.getDetalles()) {
            if (detalle.getCantidadEscaneada() < detalle.getCantidad()) {
                return false; 
            }
        }
        return true; 
    }

    public void confirmarIngresoAlmacen(OrdenDeCompra orden) {
        servicioOrden.confirmarRecepcion(orden.getId()); 
        controladorLote.iniciaVentanaLoteCerradoExito(orden);
        vista.cerrarVentana();
    }

}
