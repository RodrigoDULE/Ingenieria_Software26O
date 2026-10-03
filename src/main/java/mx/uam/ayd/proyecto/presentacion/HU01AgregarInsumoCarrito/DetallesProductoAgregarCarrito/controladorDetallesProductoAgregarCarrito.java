package mx.uam.ayd.proyecto.presentacion.HU01AgregarInsumoCarrito.DetallesProductoAgregarCarrito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.servicioCarritoCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;
import mx.uam.ayd.proyecto.presentacion.HU02CarritoPrincipal.controladorCarritoPrincipal;

/**
 * controladorDetallesProductoAgregarCarrito
 */
@Component
public class controladorDetallesProductoAgregarCarrito {

    private final servicioCarritoCompra agregarCarrito;
    private final vistaDetallesProductoAgregarCarrito vistaDetalle;

    // conectar con HU02
    private final controladorCarritoPrincipal controlCarrito;

    @Autowired
    public controladorDetallesProductoAgregarCarrito(servicioCarritoCompra agregarCarrito,
            vistaDetallesProductoAgregarCarrito vistaDetalles,
            controladorCarritoPrincipal controladorCarritoPrincipal) {
        this.agregarCarrito = agregarCarrito;
        this.controlCarrito = controladorCarritoPrincipal;
        this.vistaDetalle = vistaDetalles;
    }

    @PostConstruct
    public void inicializa() {
        vistaDetalle.setControlador(this);
    }

    public void agregarProductoaCarrito(Producto producto, int cantidad) {

        boolean bandera = agregarCarrito.agregarItem(producto, cantidad);

        if (bandera) {
            vistaDetalle.mostrarMensaje("Producto agregado correctamente");
        } else {
            vistaDetalle.mostrarMensaje("Error al agregar producto al carrito");
        }

    }

    public void visitaCarritoCompra() {
        controlCarrito.iniciaVentanaCarrito();
    }
}