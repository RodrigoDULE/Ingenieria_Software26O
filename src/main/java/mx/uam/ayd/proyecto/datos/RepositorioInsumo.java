
package mx.uam.ayd.proyecto.datos;
import org.springframework.data.repository.CrudRepository;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Insumo;


public interface RepositorioInsumo extends CrudRepository<Insumo, Long> 
{
    public Insumo findById(long id);
    public Insumo findByNombre(String nombre);
}