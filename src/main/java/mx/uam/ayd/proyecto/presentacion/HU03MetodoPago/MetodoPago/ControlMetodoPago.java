package mx.uam.ayd.proyecto.presentacion.HU03MetodoPago.MetodoPago;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import mx.uam.ayd.proyecto.negocio.servicioCarritoCompra;
import mx.uam.ayd.proyecto.negocio.ServicioCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.DireccionEnvio;
import jakarta.annotation.PostConstruct;

import mx.uam.ayd.proyecto.presentacion.HU03MetodoPago.Aprobado.ControlAprobado;

@Component
/**
 * Controlador de la pantalla de metodo de pago.
 * Recibe la direccion seleccionada, obtiene el monto total del carrito del
 * cliente asociado y coordina la transicion hacia la pantalla de aprobacion.
 */
public class ControlMetodoPago {
    private final VistaMetodoPago vistaMetodoPago;
    private final servicioCarritoCompra servicioCarritoCompra;
    private final ServicioCompra servicioCompra;
    private ControlAprobado controlAprobado;
    private double montoTotal;
    private DireccionEnvio direccionSeleccionada;

    /**
     * Crea un nuevo controlador de metodo de pago con sus dependencias
     * principales.
     *
     * @param vistaMetodoPago vista encargada de mostrar el formulario de pago.
     * @param servicioCarritoCompra servicio que permite consultar el carrito del
     *                              cliente.
     * @param controlAprobado controlador de la ventana de confirmacion.
     */
    @Autowired
    public ControlMetodoPago(VistaMetodoPago vistaMetodoPago, servicioCarritoCompra servicioCarritoCompra,
            ControlAprobado controlAprobado, ServicioCompra servicioCompra) {
        this.vistaMetodoPago = vistaMetodoPago;
        this.servicioCarritoCompra = servicioCarritoCompra;
        this.controlAprobado = controlAprobado;
        this.servicioCompra = servicioCompra;
    }

    /**
     * Vincula la vista con este controlador despues de que Spring crea el bean.
     */
    @PostConstruct
    private void inicializarControlador() {
        vistaMetodoPago.setControlador(this);
    }

    /**
     * Abre la ventana de pago y calcula el total a partir del cliente asociado a
     * la direccion seleccionada.
     *
     * @param direccionSeleccionada direccion elegida por el usuario para el envio.
     */
    public void iniciaVentanaMetodoPago(DireccionEnvio direccionSeleccionada) {
        this.direccionSeleccionada = direccionSeleccionada;
        if (direccionSeleccionada != null && direccionSeleccionada.getCliente() != null) {
            long idCliente = direccionSeleccionada.getCliente().getidCliente();
            montoTotal = servicioCarritoCompra.recuperaTotalCalculadoPorCliente(idCliente);
        } else {
            montoTotal = 0.0;
        }
        vistaMetodoPago.muestra();
    }

    /**
     * Abre la ventana de aprobacion usando el monto previamente calculado.
     */
    public void iniciaVentanaAprobado() {
        controlAprobado.muestra(montoTotal);
    }

    /**
     * Crea y persiste una nueva {@link mx.uam.ayd.proyecto.negocio.EntidadNegocio.Compra}
     * utilizando el monto calculado en este controlador y la fecha actual.
     */
    public void crearCompra() {
        servicioCompra.guardarCompra((float) montoTotal, java.time.LocalDate.now());
    }

    /**
     * Simula la conexion con la pasarela o API de pago.
     *
     * @return true si la simulacion fue exitosa.
     */
    public boolean simulacionConexionApi() {
        return servicioCarritoCompra.simulacionConexionApi();
    }

}
