package mx.uam.ayd.proyecto.datos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Evento;

public interface RepositorioEvento extends CrudRepository<Evento, Long> {

    public Evento findByIdEvento(long idEvento);

    public List<Evento> findByFechaE(LocalDate fechaE);

    public Evento findByNotificacion(LocalDate notificacion);

}
