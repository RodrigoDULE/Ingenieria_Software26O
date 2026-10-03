package mx.uam.ayd.proyecto.datos;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;

public interface repositorioProducto extends CrudRepository<Producto, Long>{
    public List<Producto> findBytipoProd(String criterio);

    public Producto findByIdProducto(long id);

    // Con este método, si buscas "espadin", te devolverá "Mezcal Espadin Joven", "Mezcla espadin", etc. **generó IA**, contINING CONTENAGA LA PALABRA
    // iGNOREcASE IGNORA SI SON MAYUSCULAS O MINUSCULAS
    List<Producto> findByNombreContainingIgnoreCase(String palabraClave);
}
