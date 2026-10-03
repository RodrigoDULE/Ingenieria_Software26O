package mx.uam.ayd.proyecto.presentacion.SerBot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.Serviciobot;

@Component
public class ControladorBot {
    
    private final Serviciobot serbot;
    private final VistaBot vsBot;
    private String mensaje;
    @Autowired
    ControladorBot(Serviciobot serbo, VistaBot vsBot){
        this.serbot = serbo;
        this.vsBot = vsBot;
    }

    @PostConstruct
    private void inyectarControlador(){
        vsBot.setControlador(this);
    }

    public void recibeMensaje(String msg){
        this.mensaje = serbot.procesaMensajeDameDatos(msg);
        System.out.println("Mensaje generado: "+ mensaje);
        vsBot.muestra(mensaje);
    }
    
    public void muestra(){
        vsBot.muestra(null);
    }
}
