package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.datos.RepositorioEmpleado;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Empleado;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de negocio para la gestión de empleados.
 * 
 * Esta clase proporciona operaciones de negocio relacionadas con empleados,
 * incluyendo la obtención de nombres de empleados y búsqueda de empleados
 * por nombre. Actúa como intermediaria entre la capa de presentación y
 * la capa de datos (repositorio).
 * 
 */
@Service
public class servicioEmpleado {

    private final RepositorioEmpleado repoEmpleado;

    /**
     * Constructor de la clase servicioEmpleado.
     * 
     * Inyecta el repositorio de empleados mediante inyección de dependencias
     * de Spring Framework. Este patrón asegura que el servicio tenga acceso
     * a la capa de datos para realizar operaciones de consulta.
     * 
     * @param repoEmpleado Repositorio de empleados inyectado por Spring
     */
    public servicioEmpleado(RepositorioEmpleado repoEmpleado) {
        this.repoEmpleado = repoEmpleado;
    }

    /**
     * Obtiene la lista de nombres de todos los empleados registrados.
     * 
     * Este método consulta el repositorio para recuperar únicamente los nombres
     * de todos los empleados disponibles en la base de datos, sin incluir
     * la información completa del empleado.
     * 
     * @return Lista de cadenas (String) conteniendo los nombres de los empleados.
     *         Retorna una lista vacía si no hay empleados registrados.
     */
    public List<String> obtenerNombreEmpleados() {
        return repoEmpleado.findAllNombres();
    }

    /**
     * Busca y obtiene empleados específicos por sus nombres.
     * 
     * Este método consulta el repositorio para encontrar todos los empleados
     * cuyos nombres coincidan con los proporcionados en la lista de entrada.
     * Es útil para búsquedas específicas de empleados cuando se conocen sus nombres.
     * 
     * @param nombresEmpleado Lista de nombres de empleados a buscar.
     *                         No debe ser nula.
     * @return Lista de objetos Empleado que coinciden con los nombres especificados.
     *         Retorna una lista vacía si no se encuentran coincidencias.
     */
    public List<Empleado> obtenerEmpleadosPorNombre(List<String> nombresEmpleado) {
        return repoEmpleado.findByNombreEmpleadoIn(nombresEmpleado);
    }
}
