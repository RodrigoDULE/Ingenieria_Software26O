package mx.uam.ayd.proyecto.datos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.DireccionEnvio;

// Capa de Acceso a Datos (Repository) para la HU-05 (Direcciones de envío).
// Centraliza las operaciones de persistencia aislando el código SQL del resto de la aplicación.
public interface RepositorioDirecciones extends CrudRepository<DireccionEnvio, Long> {

    // 1. Busca las direcciones que pertenezcan exactamente al cliente solicitado (findByCliente).
    // 2. Filtra para traer ÚNICAMENTE aquellas cuya bandera "activa" sea verdadera (AndActivaTrue).
    public List<DireccionEnvio> findByClienteAndActivaTrue(Cliente cliente);
}