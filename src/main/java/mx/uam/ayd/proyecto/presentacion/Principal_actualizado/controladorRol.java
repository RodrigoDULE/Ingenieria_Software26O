package mx.uam.ayd.proyecto.presentacion.Principal_actualizado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.presentacion.Principal.controladorPrincipal;

@Component
public class controladorRol {

    private final vistaRol vista;
    private final controladorPrincipal controlPrincipal;

    @Autowired
    public controladorRol(vistaRol vista, controladorPrincipal controlPrincipal) {
        this.vista = vista;
        this.controlPrincipal = controlPrincipal;
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
    /* 
    public void ingresaComoAdministrador() {
        vista.cierra();
        controlPrincipal.iniciaAdministrador();
    }
*/
    // perfil Cliente: pasa por la ventana principal donde ingresa su usuario
    public void ingresaComoCliente() {
        vista.cierra();
        controlPrincipal.inicia();
    }
}
