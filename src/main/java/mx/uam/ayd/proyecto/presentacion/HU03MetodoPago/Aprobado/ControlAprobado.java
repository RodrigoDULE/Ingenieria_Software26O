package mx.uam.ayd.proyecto.presentacion.HU03MetodoPago.Aprobado;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/**
 * Controlador de la pantalla de pago aprobado.
 * Recibe el monto calculado y lo expone a la vista para mostrar el resumen
 * final de la transaccion.
 */
public class ControlAprobado {
    private final VistaAprobado vistaAprobado;
    private double monto;

    /**
     * Crea un nuevo controlador asociado a la vista de aprobacion.
     *
     * @param vistaAprobado vista que renderiza la confirmacion del pago.
     */
    @Autowired
    public ControlAprobado(VistaAprobado vistaAprobado) {
        this.vistaAprobado = vistaAprobado;
    }

    /**
     * Vincula la vista con este controlador despues de la construccion del bean.
     */
    @PostConstruct
    private void inicializarControlador() {
        vistaAprobado.setControlador(this);
    }

    /**
     * Abre la vista de aprobacion con el monto resultante del pago.
     *
     * @param monto monto total aprobado.
     */
    public void muestra(double monto) {
        this.monto = monto;
        vistaAprobado.muestra();
    }

    /**
     * Devuelve el monto que la vista debe mostrar.
     *
     * @return monto total del pago aprobado.
     */
    public double getMonto() {
        return monto;
    }

}