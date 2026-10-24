package mx.uam.ayd.proyecto.datos;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto; 
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Resena;   

@Repository
public interface RepositorioResena extends CrudRepository<Resena, Long> {

    // Método automático de Spring Data: Busca las reseñas por producto
    List<Resena> findByProducto(Producto producto);
}