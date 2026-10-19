package mx.uam.ayd.proyecto.negocio;

import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioFavorito;
import mx.uam.ayd.proyecto.datos.repositorioCliente;
import mx.uam.ayd.proyecto.datos.repositorioProducto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Favorito;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;

@Service
public class ServicioFavorito {

    // repositorios que se usan para buscar clientes, productos y guardar/eliminar favoritos
    private final RepositorioFavorito repositorioFavorito;
    private final repositorioCliente repositorioCliente;
    private final repositorioProducto repositorioProducto;

    // Spring nos da los repositorios al crear este servicio.
    public ServicioFavorito(RepositorioFavorito repositorioFavorito,
            repositorioCliente repositorioCliente,
            repositorioProducto repositorioProducto) {
        this.repositorioFavorito = repositorioFavorito;
        this.repositorioCliente = repositorioCliente;
        this.repositorioProducto = repositorioProducto;
    }

    // aqui se guarda la relación entre cliente y producto si ambos existen y aún no son favoritos
    public boolean agregarFavorito(long idCliente, long idProducto) {

        // Busca las entidades para validar que los id's recibidos sí correspondan a registros reales
        Cliente cliente = repositorioCliente.findByIdCliente(idCliente);
        Producto producto = repositorioProducto.findByIdProducto(idProducto);

        // con esto no permitimos crear el favorito si falta el cliente o el producto
        if (cliente == null || producto == null) {
            return false;
        }

        // Evitamos guardar dos veces la misma combinación de cliente y producto
        if (repositorioFavorito.existsByClienteFK_IdClienteAndProductoFK_IdProducto(idCliente, idProducto)) {
            return false;
        }

        // Crea el registro que conecta al cliente con el producto y lo guarda en la base
        Favorito favorito = new Favorito();
        favorito.setClienteFK(cliente);
        favorito.setProductoFK(producto);
        repositorioFavorito.save(favorito);

        // por lo que true indica que se agregó el favorito
        return true;
    }

    // Elimina la relación de favorito del cliente con el producto, sin borrar esas entidades Cliente y Producto
    public boolean quitarFavorito(long idCliente, long idProducto) {

        // Comprueba que los id's proporcionados correspondan a un cliente y producto que ya existen
        Cliente cliente = repositorioCliente.findByIdCliente(idCliente);
        Producto producto = repositorioProducto.findByIdProducto(idProducto);

        // hace que no se pueda quitar el favorito si alguno de los registros indicados no existe.
        if (cliente == null || producto == null) {
            return false;
        }

        // aquí busca la fila de favorito que corresponde exactamente a cliente y producto
        Favorito favorito = repositorioFavorito
                .findByClienteFK_IdClienteAndProductoFK_IdProducto(idCliente, idProducto);

        // si no había relación guardada entonces no hay nada que eliminar
        if (favorito == null) {
            return false;
        }

        // Elimina solamente la relación de favorito encontrada
        repositorioFavorito.delete(favorito);

        
        return true; // si es true entonces indica que se quitó el favorito
    }
}
