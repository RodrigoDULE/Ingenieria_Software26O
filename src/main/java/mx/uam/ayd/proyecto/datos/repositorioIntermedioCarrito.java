package mx.uam.ayd.proyecto.datos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.IntermediaCarritoProd;

public interface repositorioIntermedioCarrito extends CrudRepository<IntermediaCarritoProd, Long> {
    List<IntermediaCarritoProd> findByCarIdCarrito(long id);
    IntermediaCarritoProd findByIdIntermedia(long id);
}
