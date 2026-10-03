package mx.uam.ayd.proyecto.negocio;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioDirecciones;
import mx.uam.ayd.proyecto.datos.repositorioCliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.DireccionEnvio;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.DireccionEnvio.DatosDireccion;

// Encapsula la lógica central y las reglas de negocio para la gestión de direcciones.
@Service
public class ServicioDireccionesEnvio {

    private final RepositorioDirecciones repositorioDirecciones;
    private final repositorioCliente repositorioCliente;

    // Inyección de dependencias por constructor 
    // Garantiza que el servicio no pueda ser instanciado sin sus repositorios requeridos.
    public ServicioDireccionesEnvio(RepositorioDirecciones repositorioDirecciones,
            repositorioCliente repositorioCliente) {
        this.repositorioDirecciones = repositorioDirecciones;
        this.repositorioCliente = repositorioCliente;
    }

    // Recupera exclusivamente las direcciones activas vinculadas a un cliente específico.

    public List<DireccionEnvio> obtenerListaDirecciones(Cliente cliente) {
        return repositorioDirecciones.findByClienteAndActivaTrue(cliente);
    }

    // Transforma el DTO (DatosDireccion) proveniente de la vista en una entidad de dominio real.
    // Asocia la entidad recién creada al cliente y delega su persistencia al repositorio.
    public DireccionEnvio registrarDireccion(DatosDireccion datos, Cliente cliente) {
        DireccionEnvio direccion = new DireccionEnvio(datos, cliente);
        return repositorioDirecciones.save(direccion);
    }


    // En lugar de agregar una columna a la tabla Direcciones, se actualiza la llave foránea
    // en la tabla Cliente, optimizando la consulta y garantizando que solo haya una predeterminada.
    public boolean marcarComoPredeterminada(Long idDireccion, Cliente cliente) {
        Optional<DireccionEnvio> direccionOpt = repositorioDirecciones.findById(idDireccion);
        if (!direccionOpt.isPresent()) {
            return false;
        }

        cliente.setDireccionPredeterminada(direccionOpt.get());
        repositorioCliente.save(cliente);
        return true;
    }

    // Aplica una "baja lógica" (Soft Delete) en lugar de un DELETE duro en la base de datos.
    public boolean eliminarDireccion(Long idDireccion) {
        Optional<DireccionEnvio> direccionOpt = repositorioDirecciones.findById(idDireccion);
        if (!direccionOpt.isPresent()) {
            return false;
        }

        DireccionEnvio direccion = direccionOpt.get();
        direccion.setActiva(false);
        repositorioDirecciones.save(direccion);
        return true;
    }
}