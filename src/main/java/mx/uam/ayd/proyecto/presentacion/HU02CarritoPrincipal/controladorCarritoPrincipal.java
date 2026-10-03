package mx.uam.ayd.proyecto.presentacion.HU02CarritoPrincipal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.servicioCarritoCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.carritoCompra;
import mx.uam.ayd.proyecto.presentacion.HU05CamposDeEnvio.ControlDireccionesEnvio;

@Component
public class controladorCarritoPrincipal {

    private final vistaCarritoPrincipal vistaCarritoPrincipal;
    private final servicioCarritoCompra servicioCarritoCompra;
    private final ControlDireccionesEnvio controlDirecciones;

    @Autowired
    public controladorCarritoPrincipal(vistaCarritoPrincipal vistaCarritoPrincipal,
            servicioCarritoCompra servicioCarritoCompra, ControlDireccionesEnvio controlDirecciones) {
        this.vistaCarritoPrincipal = new vistaCarritoPrincipal();
        this.servicioCarritoCompra = servicioCarritoCompra;
        this.controlDirecciones = controlDirecciones;
    }

    @PostConstruct
    private void inicializarControlador() {
        vistaCarritoPrincipal.setControlador(this);
    }

    public void iniciaVentanaCarrito() {
        carritoCompra car = servicioCarritoCompra.recuperaProductoEnCarrito();
        if (car != null) {
            vistaCarritoPrincipal.muestraCarrito(car);
        } else {
            vistaCarritoPrincipal.mostrarMensaje("Error, el cliente no tiene carrito activo");
        }
    }

    public void EliminarProd(Producto prod) {
        servicioCarritoCompra.EliminarProdCarrito(prod);
    }

    public void irDireccionEnvio() {
        // recuperamos el objeto Usuario
        Cliente client = servicioCarritoCompra.recuperaClienToDireccion();
        controlDirecciones.iniciaVentana(client);
    }
}
