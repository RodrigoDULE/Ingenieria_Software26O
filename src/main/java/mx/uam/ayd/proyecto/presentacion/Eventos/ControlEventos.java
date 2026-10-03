package mx.uam.ayd.proyecto.presentacion.Eventos;

import java.util.List;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import mx.uam.ayd.proyecto.presentacion.HU08AgendarNuevoEvento.controlAgendarNuevoEvento;
import mx.uam.ayd.proyecto.presentacion.HU09CalendarioDeEventos.ControladorCalendarioEventos;
import mx.uam.ayd.proyecto.negocio.ServicioEvento;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Evento;

@Component
public class ControlEventos {

    private VistaEventos vista;
    private controlAgendarNuevoEvento controlAgendarNuevoEvento;
    private ServicioEvento servicioEvento;
    private ControladorCalendarioEventos controlCalendario;

    @Autowired
    public ControlEventos(VistaEventos vista, controlAgendarNuevoEvento controlAgendarNuevoEvento,
            ServicioEvento servicioEvento, ControladorCalendarioEventos controlCalendario) {
        this.vista = vista;
        this.controlAgendarNuevoEvento = controlAgendarNuevoEvento;
        this.servicioEvento = servicioEvento;
        this.controlCalendario = controlCalendario;
    }

    @PostConstruct
    public void inicializar() {
        vista.setControlador(this);
    }

    public void inicia() {
        List<Evento> listaEventos = servicioEvento.obtenerEventos();
        vista.inicia(listaEventos);
    }

    public void abrirAgendarEvento() {
        // Lógica para abrir la vista de agendar evento
        System.out.println("Abrir Agendar Evento");
        // Se le pasa a la ventana el callback para que pueda refrescar la lista de
        // eventos
        controlAgendarNuevoEvento.setRefrescarEventosCallback(this::inicia);
        controlAgendarNuevoEvento.iniciaVentanaAgendarNuevoEvento();
    }

    public void abrirCalendario() {
        // Lógica para abrir la vista del calendario
        System.out.println("Abrir Calendario");
        // VistaCalendarioEventos.muestraCalendario();
        controlCalendario.inicia();
    }

}