package mx.uam.ayd.proyecto.presentacion.Principal_actualizado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.presentacion.Principal_actualizado.controlador_sesion_admin;
import mx.uam.ayd.proyecto.presentacion.Principal_actualizado.controlador_sesion_usuario;

@Component
public class controladorRol {

    private final vistaRol vista;
    private final controlador_sesion_admin controlSesionAdmin;
    private final controlador_sesion_usuario controlSesionUsuario;


    @Autowired
    public controladorRol(vistaRol vista, controlador_sesion_admin controlSesionAdmin, controlador_sesion_usuario controlSesionUsuario) {
        this.vista = vista;
        this.controlSesionAdmin = controlSesionAdmin;
        this.controlSesionUsuario = controlSesionUsuario;
    }

    @PostConstruct
    public void inicializar() {
        vista.setControlSeleccionRol(this);
    }

    // punto de entrada de la app: muestra la seleccion de perfil
    public void inicia() {
        vista.muestra();
    }

    
    // perfil Administrador: entra directo al panel principal con todas las opciones
    
    public void ingresaComoAdministrador() {
        vista.cierra();
        controlSesionAdmin.inicia();
    }

    // perfil Cliente: pasa por la ventana principal donde ingresa su usuario
    public void ingresaComoCliente() {
        vista.cierra();
        //controlSesionUsuario.inicia();
    }
}
