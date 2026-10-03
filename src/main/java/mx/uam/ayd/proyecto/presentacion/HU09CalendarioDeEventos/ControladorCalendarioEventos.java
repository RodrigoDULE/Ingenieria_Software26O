package mx.uam.ayd.proyecto.presentacion.HU09CalendarioDeEventos;

import java.time.YearMonth;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.ServicioEvento;
import mx.uam.ayd.proyecto.negocio.servicioCalendario;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Evento;


@Component
public class ControladorCalendarioEventos {

    private final VistaCalendarioEventos vistaCalendarioEventos;
    private final servicioCalendario servicioCalendario;
    private final VistaEditarNotificacion vistaEditarNotificacion;

    //private YearMonth mesActual;
   
    public void inicia() {
        vistaCalendarioEventos.setControlador(this);   
        vistaCalendarioEventos.muestraCalendario(); 
    }
    
    public void iniciaVentanaCalendario(){
        vistaCalendarioEventos.muestraCalendario();
        seleccionarFecha(LocalDate.now());
    }
    

    @Autowired
    public ControladorCalendarioEventos(VistaCalendarioEventos vistaCalendarioEventos, servicioCalendario servicioCalendario, VistaEditarNotificacion vistaEditarNotificacion){
        this.vistaCalendarioEventos = vistaCalendarioEventos;
        this.servicioCalendario=servicioCalendario;
        this.vistaEditarNotificacion= vistaEditarNotificacion;
    }

    @PostConstruct
    private void inicializarControlador(){
        vistaCalendarioEventos.setControlador(this);
        vistaEditarNotificacion.setControlador(this);
    }

    public void seleccionarFecha(LocalDate fecha){
        List<Evento> eventos = servicioCalendario.recuperaEventoporFecha(fecha);
        vistaCalendarioEventos.mostrarEventos(eventos);

    }
    public void editarNotificacionEvento(Evento evento){
        if (evento != null && vistaEditarNotificacion != null) {
            // Llama al método de la ventana modal pasándole el evento a editar
            vistaEditarNotificacion.muestra(evento); 
            System.out.println("editando");
        }

    }

    public void guardarNotificacion(Evento evento, int numdias, int numsemanas, LocalDate nuevaNotificacion) {
    try {
        // Invocamos a tu servicio con los 4 parámetros en el orden exacto
        servicioCalendario.administrarNotificaciones(evento, numdias, numsemanas, nuevaNotificacion);

        // Refrescamos la vista para mostrar los cambios
        seleccionarFecha(evento.getFechaE());

    } catch (IllegalArgumentException e) {
        System.err.println("Error: La fecha de notificación no puede ser posterior a la fecha del evento.");
    }
}


    
    
}
