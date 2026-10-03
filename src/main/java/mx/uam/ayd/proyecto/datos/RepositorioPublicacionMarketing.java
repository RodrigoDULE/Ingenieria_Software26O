package mx.uam.ayd.proyecto.datos;


import org.springframework.data.repository.CrudRepository;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.PublicacionMarketing;

public interface RepositorioPublicacionMarketing extends CrudRepository<PublicacionMarketing, Long> {

    public PublicacionMarketing findByIdPublicacion(long idPublicacion);

}
