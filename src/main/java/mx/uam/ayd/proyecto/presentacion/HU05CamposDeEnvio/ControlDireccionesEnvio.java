package mx.uam.ayd.proyecto.presentacion.HU05CamposDeEnvio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import mx.uam.ayd.proyecto.negocio.ServicioDireccionesEnvio;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.DireccionEnvio;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.DireccionEnvio.DatosDireccion;

// Importamos el controlador de la HU-03 (Método de Pago) para la integración de módulos.
import mx.uam.ayd.proyecto.presentacion.HU03MetodoPago.MetodoPago.ControlMetodoPago;


// Orquesta los flujos de negocio: listar, registrar, marcar como predeterminada y eliminar.
// Actúa como intermediario estricto entre la VistaDireccionesEnvio y el ServicioDireccionesEnvio.
@Component
public class ControlDireccionesEnvio {

    private final VistaDireccionesEnvio vistaDireccionesEnvio;
    private final ServicioDireccionesEnvio servicioDireccionesEnvio;
    private final ControlMetodoPago controlMetodoPago;

    private Cliente clienteActivo;

    // Spring Boot se encarga de instanciar y proveer la vista, el servicio local y el controlador externo.
    @Autowired
    public ControlDireccionesEnvio(VistaDireccionesEnvio vistaDireccionesEnvio,
            ServicioDireccionesEnvio servicioDireccionesEnvio,
            ControlMetodoPago controlMetodoPago) {
        this.vistaDireccionesEnvio = vistaDireccionesEnvio;
        this.servicioDireccionesEnvio = servicioDireccionesEnvio;
        this.controlMetodoPago = controlMetodoPago;
    }

    // Garantiza que la vista conozca a su controlador inmediatamente después de la inyección de dependencias.
    @PostConstruct
    private void inicializarControlador() {
        vistaDireccionesEnvio.setControlador(this);
    }

    // Recibe el contexto del cliente actual y detona la actualización de la interfaz.
    public void iniciaVentana(Cliente cliente) {
        this.clienteActivo = cliente;
        actualizarListaDirecciones();
    }

    // Recibe los datos empaquetados desde la vista, delega el guardado al servicio
    // y refresca la interfaz para que el usuario vea su nueva dirección inmediatamente.
    public void procesarRegistroDireccion(DatosDireccion datos) {
        servicioDireccionesEnvio.registrarDireccion(datos, clienteActivo);
        actualizarListaDirecciones();
    }

    // Actualiza el estado de una dirección en la base de datos a través del servicio.
    public void establecerPredeterminada(long idDireccion) {
        boolean exito = servicioDireccionesEnvio.marcarComoPredeterminada(idDireccion, clienteActivo);
        if (!exito) {
            vistaDireccionesEnvio.mostrarMensaje("No se encontró la dirección seleccionada.");
            return;
        }
        actualizarListaDirecciones();
    }

    // Solicita la eliminación de una dirección.
    // Si falla, notifica a la vista.
    public void solicitarEliminarDireccion(long idDireccion) {
        boolean exito = servicioDireccionesEnvio.eliminarDireccion(idDireccion);
        if (!exito) {
            vistaDireccionesEnvio.mostrarMensaje("No se encontró la dirección a eliminar.");
            return;
        }
        actualizarListaDirecciones();
    }

    // Método auxiliar que sincroniza el estado de la base de datos con la interfaz de usuario.
    // Recupera la lista actualizada del servicio y se la inyecta a la vista.
    private void actualizarListaDirecciones() {
        List<DireccionEnvio> direcciones = servicioDireccionesEnvio.obtenerListaDirecciones(clienteActivo);
        vistaDireccionesEnvio.muestraDirecciones(direcciones, clienteActivo);
    }


    // Transición de módulo. Al confirmar la dirección de envío,
    // cede el control de la aplicación al controlador de pagos, pasándole el contexto necesario.
    public void continuarAlPago(DireccionEnvio direccionSeleccionada) {
        controlMetodoPago.iniciaVentanaMetodoPago(direccionSeleccionada);
    }
}