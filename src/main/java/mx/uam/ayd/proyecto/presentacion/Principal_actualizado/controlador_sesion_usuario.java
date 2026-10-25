package mx.uam.ayd.proyecto.presentacion.Principal_actualizado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.conffigPD.gestionCliente;
import mx.uam.ayd.proyecto.negocio.ServicioCliente;
import mx.uam.ayd.proyecto.negocio.ServicioOrdenDeCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra;
import mx.uam.ayd.proyecto.presentacion.HU01AgregarInsumoCarrito.catalogoMezicuil.controladorCatalogoMezicuil;
import mx.uam.ayd.proyecto.presentacion.Eventos.ControlEventos;
import mx.uam.ayd.proyecto.presentacion.HU_06.ControladorOrdenesPendientes;
import mx.uam.ayd.proyecto.presentacion.HU_07.ControladorEscaner;
import mx.uam.ayd.proyecto.presentacion.HU_01_AltaProducto.ControladorAltaProducto;

@Component
public class controlador_sesion_usuario {

    private final ServicioCliente servicioCliente;
    private final ServicioOrdenDeCompra servicioOrden;
    private final ControladorEscaner controladorEscaner;
    private final controladorCatalogoMezicuil controlTiendaLinea;
    private final vista_sesion_usuario ventanUsuario;
    private final ControlEventos controlEventos;
    private final ControladorOrdenesPendientes controladorOrdenes;

    private Cliente sesionActiva;

    @Autowired
    public controlador_sesion_usuario(controladorCatalogoMezicuil controlTiendaLinea, vista_sesion_usuario ventanAdmin,
            ServicioCliente servicioCliente,ControlEventos controlEventos, 
            ControladorOrdenesPendientes controladorOrdenes,
            ServicioOrdenDeCompra servicioOrden, ControladorEscaner controladorEscaner,
             ControladorAltaProducto controladorAltaProducto, vista_sesion_usuario ventanUsuario) {
        this.controlTiendaLinea = controlTiendaLinea;
        this.ventanUsuario = ventanUsuario;
        this.servicioCliente = servicioCliente;
        
        this.controlEventos = controlEventos;
        this.controladorOrdenes = controladorOrdenes;
        this.servicioOrden = servicioOrden;
        this.controladorEscaner = controladorEscaner;

    }

    // inicializamos la ventana
    @PostConstruct
    public void inicializar() {
        ventanUsuario.setControlPrincipal(this);
    }

    public void inicia() {
        ventanUsuario.muestra(null);
    }

    public void visitaTiendaLinea() {
        controlTiendaLinea.inicia();
    }



    public void Eventos() {
        controlEventos.inicia();
    }


    public void buscaCliente(String Nombre) {

        sesionActiva = servicioCliente.dameCliente(Nombre);
        if (sesionActiva != null) {
            // establecemos el id del usuario en el gestionCliente
            gestionCliente.getInstance().iniciarSesion(sesionActiva.getidCliente());
            System.out.println(
                    "EL id del usuaario que ingreso al sistema es: " + gestionCliente.getInstance().getIdActivo());
            ventanUsuario.muestra(sesionActiva.getNombre());
        } else {
            ventanUsuario.mostrarMensaje("Ingresa un usuario registrado.");
            ventanUsuario.muestra(null);
        }
    }
}