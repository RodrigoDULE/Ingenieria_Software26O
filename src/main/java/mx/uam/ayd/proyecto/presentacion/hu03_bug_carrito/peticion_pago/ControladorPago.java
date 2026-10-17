package mx.uam.ayd.proyecto.presentacion.hu03_bug_carrito.peticion_pago;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.conffigPD.GestionEscena;
import mx.uam.ayd.proyecto.negocio.ServicioPago;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.carritoCompra;
import mx.uam.ayd.proyecto.presentacion.hu03_bug_carrito.pago_exitoso.ControladorExitoPago;

@Component 
public class ControladorPago {

    private final VistaPago vistaPago;
    private final ServicioPago servicioPago;
    private final ControladorExitoPago controladorExitoPago;
    
    @Autowired 
    ControladorPago(VistaPago vistaPago, ServicioPago servicioPago, ControladorExitoPago controladorExitoPago){
        this.vistaPago = vistaPago;
        this.servicioPago = servicioPago;
        this.controladorExitoPago = controladorExitoPago;
    }


    @PostConstruct 
    private void inicializarControlador(){
        vistaPago.setControlador(this);
    }

    public void pagarCarritoCompra(carritoCompra carritoCompra){
        boolean pagado = servicioPago.peticionPagarCarrito(carritoCompra);

        if(!pagado){
            System.out.println("No se ha podido pagar el carrito");
            vistaPago.mostrarMensaje("Ocurrio un error en el pago del carrito, por favor reinicie las ventanas");
            return ;
        }else{

            controladorExitoPago.muestraVentanaExito(carritoCompra);
            
        }

    }
    
    public void iniciaVentanaMetodoPago(long idUsuario){
        carritoCompra car = servicioPago.recuperaCarritoCompra(idUsuario);

        if(car == null){
            return ;
        }

        vistaPago.muestra(car, GestionEscena.getInstEscena().getStage());
    }
}