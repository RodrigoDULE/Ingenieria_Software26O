package mx.uam.ayd.proyecto.presentacion.HU04FormularioMarketing;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import mx.uam.ayd.proyecto.negocio.ServicioFormularioMarketing;
import mx.uam.ayd.proyecto.negocio.ServicioGeneracionContenido;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.FormularioMarketing;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.FormularioMarketing.DatosFormulario;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.FormularioMarketing.TipoContenido;
import mx.uam.ayd.proyecto.presentacion.HU10GuardarContenido.ControladorGuardarContenido;
import mx.uam.ayd.proyecto.negocio.ServicioGeneracionContenido.VariacionContenido;


// Gestiona el registro mediante los servicios de negocio, solicita la generación de variaciones 
// y realizar la transición hacia la HU-10 (Guardar Contenido).
@Component
public class ControlFormularioMarketing {

    private final VistaFormularioMarketing vistaFormularioMarketing;
    private final ServicioFormularioMarketing servicioFormularioMarketing;
    private final ServicioGeneracionContenido servicioGeneracionContenido;
    private final ControladorGuardarContenido controlGuardarContenido;

    // Almacena el estado del formulario actual para pasarlo como contexto a la siguiente HU.
    private FormularioMarketing formularioActual;

    // Constructor del controlador.
    // Utiliza inyección de dependencias de Spring (@Autowired) para inicializar 
    // la vista, los servicios de negocio y el controlador de la siguiente HU.
    @Autowired
    public ControlFormularioMarketing(VistaFormularioMarketing vistaFormularioMarketing,
            ServicioFormularioMarketing servicioFormularioMarketing,
            ServicioGeneracionContenido servicioGeneracionContenido,
            ControladorGuardarContenido controladorGuardarContenido) {
        
        this.vistaFormularioMarketing = vistaFormularioMarketing;
        this.servicioFormularioMarketing = servicioFormularioMarketing;
        this.servicioGeneracionContenido = servicioGeneracionContenido;
        this.controlGuardarContenido = controladorGuardarContenido;
    }

    // Establece el enlace bidireccional inyectando este controlador en su vista correspondiente.
    @PostConstruct
    private void inicializarControlador() {
        vistaFormularioMarketing.setControlador(this);
    }

    // Despliega la ventana principal del Formulario de Marketing.
    public void iniciaVentanaFormularioMarketing() {
        vistaFormularioMarketing.muestra();
    }

    // Registra la información en la base de datos, solicita la generación 
    // de variaciones al servicio correspondiente y actualiza la vista con los resultados.
    public void procesarGeneracion(TipoContenido tipoContenido, List<String> plataformasDestino,
            Integer cantidadVariaciones, LocalDate fechaEstimadaPublicacion, List<File> archivos) {

        DatosFormulario datos = new DatosFormulario(tipoContenido, plataformasDestino, cantidadVariaciones, fechaEstimadaPublicacion);

        // Se registra el formulario y se guarda la referencia actual
        FormularioMarketing formularioGuardado = servicioFormularioMarketing.registrarFormulario(datos, archivos);
        formularioActual = formularioGuardado;

        // Se actualiza el estado de la vista
        vistaFormularioMarketing.habilitarBotonGenerar();

        // Se generan y muestran las variaciones de contenido
        List<VariacionContenido> listaVariaciones = servicioGeneracionContenido.generarVariaciones(formularioGuardado);
        vistaFormularioMarketing.mostrarListaVariaciones(formularioGuardado, listaVariaciones);
    }
    // Cierra la pantalla actual de marketing y transfiere el flujo a la HU-10,
    public void seleccionarVariacion(VariacionContenido variacionElegida) {
        
        vistaFormularioMarketing.cerrarVentana();

        controlGuardarContenido.iniciaVentanaGuardarContenido(
            formularioActual,
            variacionElegida
        );
    }
}