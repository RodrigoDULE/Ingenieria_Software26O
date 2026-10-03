package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;

public interface repositorioCliente extends CrudRepository<Cliente, Long>{
    public Cliente findByNombre(String nombre);
    public Cliente findByIdCliente(long idUsuario);
}
