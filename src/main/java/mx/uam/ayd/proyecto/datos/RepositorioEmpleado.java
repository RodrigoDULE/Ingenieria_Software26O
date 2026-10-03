package mx.uam.ayd.proyecto.datos;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Empleado;

public interface RepositorioEmpleado extends CrudRepository<Empleado, Long> {

    public Empleado findByIdEmpleado(long idEmpleado);

    public List<Empleado> findByNombreEmpleadoIn(List<String> nombresEmpleado);

    @Query("SELECT e.nombreEmpleado FROM Empleado e")
    public List<String> findAllNombres();

}
