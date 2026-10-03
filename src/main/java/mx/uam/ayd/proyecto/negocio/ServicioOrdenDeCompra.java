package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.datos.RepositorioOrdenDeCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra.EstadoOrden;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ServicioOrdenDeCompra {

    private final RepositorioOrdenDeCompra repositorioOrden;

    public ServicioOrdenDeCompra(RepositorioOrdenDeCompra repositorioOrden) {
        this.repositorioOrden = repositorioOrden;
    }

    
    // Bucamos recuperar las órdenes creadas que el usuario debe revisar al entrar a la pantalla
    // por defalut nos va a mostrar las ordentende que estén pendientes por revisar

    public List<OrdenDeCompra> obtenerOrdenesPendientes() {
        return repositorioOrden.findByEstado(EstadoOrden.REVISION_PENDIENTE);
    }

    
    // De ellas, recuperamos una orden específica para ver sus detalles.
    // La orden que queremos autorizar en ese momento.
   
    public OrdenDeCompra obtenerDetallesDeOrden(Long idOrden) 
    {
        return repositorioOrden.findById(idOrden).orElse(null); 
    }

    
    // Autorizamos la orden, cambiamos su estado y calculamos la fecha de entrega.

    public OrdenDeCompra autorizarOrden(Long idOrden) 
    {

        // Buscamos la orden
        OrdenDeCompra orden = obtenerDetallesDeOrden(idOrden);

        if (orden != null && orden.getEstadoOrden() == EstadoOrden.REVISION_PENDIENTE) {
            
            orden.setEstadoOrden(EstadoOrden.AUTORIZADA);
            orden.setFechaEnvio(LocalDate.now()); 
            
            // definimos que la fecha estimada de recepción sea en 5 días por ejemplo
            orden.setFechaRecepcion(LocalDate.now().plusDays(5)); 

            // Guardamos los cambios en la base de datos
            return repositorioOrden.save(orden); 
        }

        // Si la orden no existe o ya estaba autorizada, retornamos null o lanzamos un error
        return null; 
    }


    public OrdenDeCompra confirmarRecepcion(Long idOrden) 
    {
        OrdenDeCompra orden = obtenerDetallesDeOrden(idOrden);

        // Verificamos que exista y que haya estado autorizada
        if (orden != null && orden.getEstadoOrden() == EstadoOrden.AUTORIZADA) {
            
            // Cambiamos el estado para indicar que ya llegó al almacén
            orden.setEstadoOrden(EstadoOrden.RECIBIDA); 

            return repositorioOrden.save(orden);
        }
        return null; 
    }
}