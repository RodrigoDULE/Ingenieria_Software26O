package mx.uam.ayd.proyecto.presentacion.Principal_actualizado;

import mx.uam.ayd.proyecto.negocio.ServicioAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.conffigPD.gestionCliente;

import mx.uam.ayd.proyecto.negocio.ServicioOrdenDeCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Administrador;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra;
import mx.uam.ayd.proyecto.presentacion.HU01AgregarInsumoCarrito.catalogoMezicuil.controladorCatalogoMezicuil;
import mx.uam.ayd.proyecto.presentacion.HU04FormularioMarketing.ControlFormularioMarketing;
import mx.uam.ayd.proyecto.presentacion.Eventos.ControlEventos;
import mx.uam.ayd.proyecto.presentacion.HU_06.ControladorOrdenesPendientes;
import mx.uam.ayd.proyecto.presentacion.HU_07.ControladorEscaner;
import mx.uam.ayd.proyecto.presentacion.HU_01_AltaProducto.ControladorAltaProducto;

@Component
public class controlador_sesion_admin {

    private final ServicioAdmin servicioAdmin;
    private final ServicioOrdenDeCompra servicioOrden;
    private final ControladorEscaner controladorEscaner;
    private final controladorCatalogoMezicuil controlTiendaLinea;
    private final vista_sesion_admin ventanAdmin;
    private final ControlFormularioMarketing controlFormularioMarketing;
    private final ControlEventos controlEventos;
    private final ControladorOrdenesPendientes controladorOrdenes;
    private final ControladorAltaProducto controladorAltaProducto;

    private Administrador sesionActiva;

    @Autowired
    public controlador_sesion_admin(controladorCatalogoMezicuil controlTiendaLinea, vista_sesion_admin ventanAdmin,
            ControlFormularioMarketing controlFormularioMarketing,
            ControlEventos controlEventos, ControladorOrdenesPendientes controladorOrdenes,
            ServicioOrdenDeCompra servicioOrden, ControladorEscaner controladorEscaner, ControladorAltaProducto controladorAltaProducto, ServicioAdmin servicioAdmin) {
        this.controlTiendaLinea = controlTiendaLinea;
        this.ventanAdmin = ventanAdmin;
        this.controlFormularioMarketing = controlFormularioMarketing;
        this.controlEventos = controlEventos;
        this.controladorOrdenes = controladorOrdenes;
        this.servicioOrden = servicioOrden;
        this.controladorEscaner = controladorEscaner;
        this.controladorAltaProducto = controladorAltaProducto;
        this.servicioAdmin = servicioAdmin;
    }

    // inicializamos la ventana
    @PostConstruct
    public void inicializar() {
        ventanAdmin.setControlPrincipal(this);
    }

    public void inicia() {
        ventanAdmin.muestra(null);
    }

    public void visitaTiendaLinea() {
        controlTiendaLinea.inicia();
    }

    public void revisarInventario(){
        controladorAltaProducto.inicia();
    }

    public void Eventos() {
        controlEventos.inicia();
    }

    public void abreFormularioMarketing() {
        controlFormularioMarketing.iniciaVentanaFormularioMarketing();
    }

    // control hacia HU-6 de Jean

    public void irAVentanaOrdenesCreadas() {
        controladorOrdenes.iniciaVentanaOrdenesCreadas();
    }

    // fin HU-6

    // conexión con HU-7

    public void iniciarRecepcion(String OrdenId)
    {
        try{
            Long id = Long.parseLong(OrdenId);

            OrdenDeCompra orden = servicioOrden.obtenerDetallesDeOrden(id);

            if(orden != null && orden.getEstadoOrden() == OrdenDeCompra.EstadoOrden.ENVIADA)
            {
                controladorEscaner.iniciaVentanaEscaner(orden);
            }else {
               
                System.out.println("La orden no existe o no tiene estado de ENVIADA.");
            }
        }catch (NumberFormatException e) {
            System.out.println("Error, por favor ingresa un número de ID válido.");
        }

    }

    // FIN hu - 7

    public void buscaAdmin(String nombre, String contrasena) {
        Administrador admin = servicioAdmin.dameAdmin(nombre, contrasena);
        if (admin != null) {
            sesionActiva = servicioAdmin.dameAdmin(nombre, contrasena);
            if (sesionActiva != null) {
                gestionCliente.getInstance().iniciarSesion(sesionActiva.getidAdmin());
            }
                ventanAdmin.muestra(admin.getNombre());      // aquí se activan los botones
        }else {
                ventanAdmin.mostrarMensaje("Usuario o contraseña incorrectos.");
                ventanAdmin.muestra(null);                   // aquí se vuelven a bloquear
            }
    }
   
}