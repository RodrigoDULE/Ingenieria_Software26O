package mx.uam.ayd.proyecto.presentacion.HU08AgendarNuevoEvento;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Empleado;
import mx.uam.ayd.proyecto.negocio.ServicioEvento;
import mx.uam.ayd.proyecto.negocio.servicioEmpleado;

/**
 * Controlador del caso de uso para agendar un nuevo evento.
 *
 * <p>
 * Coordina la interacción entre la vista y los servicios de negocio para
 * validar disponibilidad, construir datos del evento y registrarlo.
 * </p>
 */
@Component
public class controlAgendarNuevoEvento {
    private final vistaAgendarNuevoEvento vistaAgendarNuevoEvento;
    private final ServicioEvento servicioEvento;
    private final servicioEmpleado servicioEmpleado;
    private Runnable refrescarEventosCallback;

    /**
     * Crea el controlador con sus dependencias de vista y servicios.
     *
     * @param vistaAgendarNuevoEvento vista de agendar nuevo evento
     * @param servicioEvento          servicio de eventos
     * @param servicioEmpleado        servicio de empleados
     */
    @Autowired
    public controlAgendarNuevoEvento(vistaAgendarNuevoEvento vistaAgendarNuevoEvento,
            ServicioEvento servicioEvento, servicioEmpleado servicioEmpleado) {
        this.vistaAgendarNuevoEvento = vistaAgendarNuevoEvento;
        this.servicioEvento = servicioEvento;
        this.servicioEmpleado = servicioEmpleado;
    }

    /**
     * Inicia y muestra la ventana para agendar un nuevo evento.
     */
    public void iniciaVentanaAgendarNuevoEvento() {
        vistaAgendarNuevoEvento.muestra();
    }

    /**
     * Inicializa el enlace entre la vista
     */
    @PostConstruct
    public void inicializar() {
        vistaAgendarNuevoEvento.setControladorAgendarNuevoEvento(this);
    }

    /**
     * Configura el callback para refrescar la lista de eventos cuando se registre
     * uno nuevo.
     *
     * @param refrescarEventosCallback acción a ejecutar tras guardar el evento
     */
    public void setRefrescarEventosCallback(Runnable refrescarEventosCallback) {
        this.refrescarEventosCallback = refrescarEventosCallback;
    }

    /**
     * Obtiene los nombres de empleados disponibles para asignar al evento.
     *
     * @return lista de nombres de empleados
     */
    public List<String> obtenerNombresEmpleados() {
        return servicioEmpleado.obtenerNombreEmpleados();
    }

    /**
     * Obtiene las entidades {@link Empleado} a partir de sus nombres.
     *
     * @param nombresEmpleados nombres seleccionados en la vista
     * @return lista de empleados correspondientes
     */
    public List<Empleado> obtenerEmpleadosSeleccionados(List<String> nombresEmpleados) {
        return servicioEmpleado.obtenerEmpleadosPorNombre(nombresEmpleados);
    }

    /**
     * Registra un nuevo evento y notifica a la vista para refrescar la lista si
     * existe callback configurado.
     *
     * @param nombre           nombre del evento
     * @param tipo             tipo de evento
     * @param fecha            fecha del evento
     * @param horaInicio       hora de inicio en formato {@code HH:mm}
     * @param horaFin          hora de finalización en formato {@code HH:mm}
     * @param acuerdo          tipo de acuerdo del evento
     * @param lugar            ubicación del evento
     * @param notas            notas adicionales del evento
     * @param noAsistentes     número de asistentes estimados
     * @param comision         comisión calculada para el evento
     * @param nombresEmpleados nombres de los empleados asignados
     */
    public void agregarEvento(String nombre, String tipo, LocalDate fecha,
            String horaInicio, String horaFin, String acuerdo, String lugar, String notas, int noAsistentes,
            int comision, List<String> nombresEmpleados) {

        LocalDate notificacion = servicioEvento.calcularUnaSemanaAntes(fecha);
        List<Empleado> empleados = obtenerEmpleadosSeleccionados(nombresEmpleados);
        servicioEvento.agregarEvento(
                nombre,
                tipo,
                fecha,
                horaInicio,
                horaFin,
                acuerdo,
                lugar,
                notas,
                noAsistentes,
                comision,
                notificacion,
                empleados);

        // Notifica a la vista para refrescar la lista de eventos si se ha configurado
        // un callback
        if (refrescarEventosCallback != null) {
            refrescarEventosCallback.run();
        }
    }

    /**
     * Verifica la disponibilidad de horario para la fecha y rango de horas
     * indicado.
     *
     * @param fecha      fecha a consultar
     * @param horaInicio hora de inicio propuesta
     * @param horaFin    hora de finalización propuesta
     * @return {@code true} si el horario está disponible; en caso contrario,
     *         {@code false}
     */
    public boolean verificarDisponibilidad(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        return servicioEvento.verificarDisponibilidad(fecha, horaInicio, horaFin);
    }

}