package mx.uam.ayd.proyecto.presentacion.HU_06;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra;
import org.springframework.context.annotation.Lazy;

@Component
public class ControladorVentanaExito {
    
    private final VistaVentanaExito vistaVentanaExito;
    private final ControladorOrdenesPendientes controladorOrdenes; // El controlador al que volveremos
    
    @Autowired
    public ControladorVentanaExito(@Lazy ControladorOrdenesPendientes controladorOrdenes, VistaVentanaExito vistaVentanaExito) 
    {
        this.vistaVentanaExito = vistaVentanaExito;
        this.controladorOrdenes = controladorOrdenes;
    }

    @PostConstruct
    private void inicializarControlador() {
        vistaVentanaExito.setControlador(this);
    }

    // AHORA RECIBIMOS EL STAGE ACTUAL (La ventana que ya está abierta)
    public void iniciaVentanaExito(Stage ventanaActual, OrdenDeCompra orden, String nombreProveedor) {
        // Le pasamos la ventana existente a la vista para que no cree una nueva
        vistaVentanaExito.muestraExito(ventanaActual, orden, nombreProveedor);
        
    }

    public void volverAOrdenesPendientes(Stage ventanaActual) {
        
        controladorOrdenes.iniciaVentanaOrdenesCreadas(); 
        ventanaActual.close();
    }
}
