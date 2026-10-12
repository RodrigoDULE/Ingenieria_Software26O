package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.EntidadNegocio.DetallesCompra;

public interface RepositorioDetallesCompra extends CrudRepository<DetallesCompra, Long> {
    
}
