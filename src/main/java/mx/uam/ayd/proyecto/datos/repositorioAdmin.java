package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Administrador;

public interface repositorioAdmin extends CrudRepository<Administrador, Long> {
    public Administrador findByNombre(String nombre);
    public Administrador findByIdAdmin(long idUsuario);
    public Administrador findByNombreAndContraseña(String nombre, String contraseña);
}
