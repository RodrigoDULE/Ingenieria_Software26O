package mx.uam.ayd.proyecto.presentacion.hu03_bug_carrito.pago_exitoso;

//GEnerado por IA
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.carritoCompra;
//GEnerado por IA
import mx.uam.ayd.proyecto.presentacion.VolverAlCatalogoEvent;

@Component 
public class ControladorExitoPago {
    private final VistaExitoPago vistaExitoPago;
    //GEnerado por IA
    private final ApplicationEventPublisher eventPublisher;

    //GEnerado por IA
    ControladorExitoPago(VistaExitoPago cVistaExitoPago, ApplicationEventPublisher eventPublisher){
        this.vistaExitoPago = cVistaExitoPago;
        //GEnerado por IA
        this.eventPublisher = eventPublisher;
    }

    @PostConstruct 
    private void inicializarControlador(){
        vistaExitoPago.setControlador(this);
    }

    public void muestraVentanaExito(carritoCompra car){
        //GEnerado por IA
        vistaExitoPago.muestra(car);
    }

    //GEnerado por IA
    public void regresarAlCatalogo(){
        //GEnerado por IA
        eventPublisher.publishEvent(new VolverAlCatalogoEvent());
        //GEnerado por IA
    }

}
