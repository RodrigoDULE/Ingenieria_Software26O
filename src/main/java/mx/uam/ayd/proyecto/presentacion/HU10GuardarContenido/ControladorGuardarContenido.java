package mx.uam.ayd.proyecto.presentacion.HU10GuardarContenido;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.EntidadNegocio.FormularioMarketing;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.PublicacionMarketing;
import mx.uam.ayd.proyecto.negocio.ServicioGeneracionContenido.VariacionContenido;
import mx.uam.ayd.proyecto.negocio.ServicioGuardarContenido;
@Component
public class ControladorGuardarContenido {
    

    
    private final ServicioGuardarContenido servicioGuardar;
    private final VistaGuardarContenido vistaGuardar;
    private final VistaEditarMarketing vistaEditarMarketing;
    private FormularioMarketing formularioActual;

    



    @Autowired
    public ControladorGuardarContenido(ServicioGuardarContenido servicioGuardar,VistaGuardarContenido vistaGuardar, VistaEditarMarketing vistaEditarMarketing) {
        this.servicioGuardar = servicioGuardar;
        this.vistaGuardar = vistaGuardar;
        this.vistaEditarMarketing = vistaEditarMarketing;
    }






    /**
     * Muestra la ventana con todos los contenidos guardados.
     */
    public void iniciaVentanaGuardarContenido(
        FormularioMarketing formulario,
        VariacionContenido variacion){

        vistaGuardar.muestra();

       vistaGuardar.mostrarContenido(formulario);
}

    /**
     * Guarda un formulario.
     */
    public void guardarFormulario(FormularioMarketing formulario) {

        servicioGuardar.actualizarFormulario(formulario);

    }

    /**
     * Recupera un formulario.
     */
    public FormularioMarketing recuperarFormulario(long idFormulario) {

        return servicioGuardar.buscarFormulario(idFormulario);

    }

    /**
     * Actualiza un formulario.
     */
    public void actualizarFormulario(FormularioMarketing formulario) {

        servicioGuardar.actualizarFormulario(formulario);

    }

    /**
     * Publica un formulario.
     */
    public void publicarFormulario(long idFormulario,
                                   PublicacionMarketing publicacion) {

        servicioGuardar.publicarFormulario(idFormulario,
                                             publicacion);

    }

}

