package mx.uam.ayd.proyecto.presentacion.hu03_bug_carrito.pago_exitoso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.conffigPD.GestionEscena;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.carritoCompra;

@Component 
public class ControladorExitoPago {
    private final VistaExitoPago vistaExitoPago;

    @Autowired 
    ControladorExitoPago(VistaExitoPago cVistaExitoPago){
        this.vistaExitoPago = cVistaExitoPago;
    }

    @PostConstruct 
    private void inicializarControlador(){
        vistaExitoPago.setControlador(this);
    }

    public void muestraVentanaExito(carritoCompra car){
        vistaExitoPago.muestra(car, GestionEscena.getInstEscena().getInicial());
    }
}
