package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.carritoCompra;

public interface repositoriocarritoCompra extends CrudRepository<carritoCompra, Long> {

    public carritoCompra findByIdCarrito(long id);
}