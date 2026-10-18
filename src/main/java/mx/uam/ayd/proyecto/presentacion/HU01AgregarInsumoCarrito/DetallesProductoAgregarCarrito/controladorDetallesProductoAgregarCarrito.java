package mx.uam.ayd.proyecto.presentacion.HU01AgregarInsumoCarrito.DetallesProductoAgregarCarrito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.servicioCarritoCompra;
import mx.uam.ayd.proyecto.negocio.ServicioResena; 
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Resena;
import mx.uam.ayd.proyecto.presentacion.HU02CarritoPrincipal.controladorCarritoPrincipal;
import java.util.List;
/**
 * controladorDetallesProductoAgregarCarrito
 */
@Component
public class controladorDetallesProductoAgregarCarrito {

    private final servicioCarritoCompra agregarCarrito;
    private final vistaDetallesProductoAgregarCarrito vistaDetalle;

    // conectar con HU02
    private final controladorCarritoPrincipal controlCarrito;
    //Declaramos para la historia de usuario HU-11 
    private final ServicioResena servicioResena;

    

    @Autowired
    public controladorDetallesProductoAgregarCarrito(servicioCarritoCompra agregarCarrito,
            vistaDetallesProductoAgregarCarrito vistaDetalles,
            controladorCarritoPrincipal controladorCarritoPrincipal, ServicioResena servicioResena) {
        this.agregarCarrito = agregarCarrito;
        this.controlCarrito = controladorCarritoPrincipal;
        this.vistaDetalle = vistaDetalles;
        this.servicioResena = servicioResena;
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

    /* 
    =========================================
    Este el es nuevo concepto de la reseña, para poder ver correctamente
    correpodiente a la histoira de usuario HU-11 
    ==========================================
    
    */
   public List<Resena> obtenerResenas(Producto producto){
        return servicioResena.obtenerResenasPorProducto(producto);
   }

   public void publicarResena(Producto producto, int calificacion , String titulo, String comentario){
    try {
            // Llamamos a la lógica de negocio que valida y guarda
            servicioResena.crearResena(calificacion, titulo, comentario, producto);
            //Esto permite alojar el comenntario 
            // Usamos el método de mensajes que ya usa tu vista
            vistaDetalle.mostrarMensaje("¡Reseña publicada con éxito!");

            List<Resena> resenasActualizadas = servicioResena.obtenerResenasPorProducto(producto);
            vistaDetalle.mostrarResenasEnPantalla(resenasActualizadas);
        } catch (Exception e) {
            // Si las validaciones fallan (ej. estrellas en 0 o título vacío), se muestra aquí
            vistaDetalle.mostrarMensaje("Error: " + e.getMessage());
        }
   }


}