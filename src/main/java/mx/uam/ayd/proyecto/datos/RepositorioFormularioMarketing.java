package mx.uam.ayd.proyecto.datos;
import org.springframework.data.repository.CrudRepository;


import mx.uam.ayd.proyecto.negocio.EntidadNegocio.FormularioMarketing;


//genera automáticamente la implementación para las operaciones básicas de base de datos
public interface RepositorioFormularioMarketing extends CrudRepository<FormularioMarketing, Long> {


    // Recupera un formulario específico directamente desde la base de datos 
    // utilizando su identificador único
    public FormularioMarketing findById(long idFormulario);

}