package mx.uam.ayd.proyecto.datos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Favorito;

public interface RepositorioFavorito extends CrudRepository<Favorito, Long> {

    // Busca un favorito por su id, devuelve el registro completo de favorito
    public Favorito findByIdFavorito(long idFavorito);

    // busca los favoritos de un cliente y devuelve una lista porque el cliente puede tener más de un favorito
    List<Favorito> findByClienteFK_IdCliente(long idCliente);

    // Responde a la pregunta ¿ya existe un favorito?, por eso el boolean.
    boolean existsByClienteFK_IdClienteAndProductoFK_IdProducto(long idCliente, long idProducto);

    //devuelve el objeto "Favorito" correspondiente a un cliente y producto
    Favorito findByClienteFK_IdClienteAndProductoFK_IdProducto(long idCliente, long idProducto);
}
