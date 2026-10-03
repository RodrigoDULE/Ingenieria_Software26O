package mx.uam.ayd.proyecto.negocio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import mx.uam.ayd.proyecto.datos.RepositorioEvento;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Empleado;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Evento;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de negocio para gestionar operaciones relacionadas con eventos.
 *
 * <p>
 * Encapsula reglas de negocio de disponibilidad, cálculo de notificación y
 * persistencia de eventos mediante el repositorio.
 * </p>
 */
@Service
public class ServicioEvento {
    /**
     * Repositorio de eventos para acceso a persistencia.
     */
    private final RepositorioEvento repoEvento;

    /**
     * Construye el servicio con su dependencia de repositorio.
     *
     * @param repoEvento repositorio de eventos
     */
    public ServicioEvento(RepositorioEvento repoEvento) {
        this.repoEvento = repoEvento;
    }

    /**
     * Obtiene todos los eventos almacenados.
     *
     * @return lista con todos los eventos registrados
     */
    public List<Evento> obtenerEventos() {
        List<Evento> listaEventos = new ArrayList<Evento>();

        for (Evento evento : repoEvento.findAll()) {
            listaEventos.add(evento);
        }

        return listaEventos;
    }

    /**
     * Registra un evento nuevo en la base de datos.
     *
     * <p>
     * Convierte horas desde texto, crea la entidad de evento, asigna sus
     * atributos y la persiste junto con empleados relacionados.
     * </p>
     *
     * @param nombre       nombre del evento
     * @param tipo         tipo de evento
     * @param fecha        fecha en la que se realizará el evento
     * @param horaInicio   hora de inicio en formato {@code HH:mm}
     * @param horaFin      hora de finalización en formato {@code HH:mm}
     * @param acuerdo      acuerdo económico del evento
     * @param lugar        lugar del evento
     * @param notas        notas adicionales
     * @param noAsistentes número de asistentes esperados
     * @param comision     comisión asociada al evento
     * @param notificacion fecha de notificación previa al evento
     * @param empleados    empleados asignados al evento
     */
    @Transactional
    public boolean agregarEvento(String nombre, String tipo, LocalDate fecha,
            String horaInicio, String horaFin, String acuerdo,
            String lugar, String notas, int noAsistentes, int comision, LocalDate notificacion,
            List<Empleado> empleados) {

        LocalTime horaIn = LocalTime.parse(horaInicio);
        LocalTime horaFin_ = LocalTime.parse(horaFin);

        Evento evento = new Evento();
        evento.setNombreEvento(nombre);
        evento.setTipoEvento(tipo);
        evento.setFechaE(fecha);
        evento.setHoraIn(horaIn);
        evento.setHoraFin(horaFin_);
        evento.setAcuerdoEconomico(acuerdo);
        evento.setLugar(lugar);
        evento.setNotasAdicionales(notas);
        evento.setNoAsistentes(noAsistentes);
        evento.setComision(comision);
        evento.setNotificacion(notificacion);
        evento.getEmpleados().addAll(empleados);

        repoEvento.save(evento);

        return true;
    }

    /**
     * Verifica si un horario propuesto está disponible para agendar un evento.
     *
     * <p>
     * Reglas aplicadas:
     * </p>
     * <p>
     * 1) La duración mínima del evento debe ser de 2 horas.
     * </p>
     * <p>
     * 2) No debe existir solapamiento con otros eventos del mismo día.
     * </p>
     * <p>
     * 3) Debe existir al menos 6 horas de separación con cualquier evento del
     * mismo día.
     * </p>
     *
     * @param fecha      fecha propuesta para el evento
     * @param horaInicio hora de inicio propuesta
     * @param horaFin    hora de finalización propuesta
     * @return {@code true} si cumple todas las reglas; en caso contrario,
     *         {@code false}
     */
    public boolean verificarDisponibilidad(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        Duration duracion = Duration.between(horaInicio, horaFin);
        if (duracion.toMinutes() < 120) {
            return false;
        }
        LocalDate fechaActual = LocalDate.now();
        if (fecha.isBefore(fechaActual)) {
            return false;

        }
        for (Evento evento : repoEvento.findAll()) {
            if (!fecha.equals(evento.getFechaE())) {
                continue;
            }

            boolean seSolapan = horaInicio.isBefore(evento.getHoraFin()) &&
                    horaFin.isAfter(evento.getHoraIn());
            if (seSolapan) {
                return false;
            }

            Duration diferencia;
            if (horaInicio.isAfter(evento.getHoraFin()) || horaInicio.equals(evento.getHoraFin())) {
                diferencia = Duration.between(evento.getHoraFin(), horaInicio);
            } else {
                diferencia = Duration.between(horaFin, evento.getHoraIn());
            }

            if (diferencia.toHours() < 6) {
                return false;
            }

        }
        return true;
    }

    /**
     * Calcula la fecha de notificación una semana antes de la fecha del evento.
     *
     * @param fecha fecha del evento
     * @return fecha correspondiente a una semana antes
     */
    public LocalDate calcularUnaSemanaAntes(LocalDate fecha) {
        return fecha.minusWeeks(1);
    }

}
