package mx.uam.ayd.proyecto.presentacion.hu03_bug_carrito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.ServicioPago;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.carritoCompra;

@Component 
public class ControladorPago {

    private VistaPago vistaPago;
    private ServicioPago servicioPago;
    
    @Autowired 
    ControladorPago(VistaPago vistaPago, ServicioPago servicioPago){
        this.vistaPago = vistaPago;
        this.servicioPago = servicioPago;
    }

    ControladorPago(){}

    @PostConstruct 
    private void inicializarControlador(){
        vistaPago.setControlador(this);
    }
    
    public void iniciaVentanaMetodoPago(long idUsuario){
        carritoCompra car = servicioPago.recuperaCarritoCompra(idUsuario);

        if(car == null){
            return ;
        }

        vistaPago.muestra(car);
    }
}