
package mx.uam.ayd.proyecto.datos;
import org.springframework.data.repository.CrudRepository;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.DetalleOrden;


public interface RepositorioDetalleOrden extends CrudRepository<DetalleOrden, Long>
{
    public DetalleOrden findById(long id);
}