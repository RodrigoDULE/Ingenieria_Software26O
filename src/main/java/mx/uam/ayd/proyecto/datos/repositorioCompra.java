package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Compra;

public interface repositorioCompra extends CrudRepository<Compra, Long> {

    public Compra findByIdCompra(long id);
}
