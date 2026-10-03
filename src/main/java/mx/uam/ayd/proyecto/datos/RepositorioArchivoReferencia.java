package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.ArchivoReferencia;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Evento;

public interface RepositorioArchivoReferencia extends CrudRepository<ArchivoReferencia, Long> {

    // Spring buscará directamente la variable 'id'
    public ArchivoReferencia findById(long id);

    // Spring buscará directamente la variable 'nombre'. 
    // (Asumiendo que la variable en la clase ArchivoReferencia se llama "nombre")
    public ArchivoReferencia findByNombre(String nombre);

}