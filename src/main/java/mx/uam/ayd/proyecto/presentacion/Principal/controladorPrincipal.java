package mx.uam.ayd.proyecto.presentacion.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.conffigPD.gestionCliente;
import mx.uam.ayd.proyecto.negocio.ServicioCliente;
import mx.uam.ayd.proyecto.negocio.ServicioOrdenDeCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra;
import mx.uam.ayd.proyecto.presentacion.HU01AgregarInsumoCarrito.catalogoMezicuil.controladorCatalogoMezicuil;
import mx.uam.ayd.proyecto.presentacion.HU04FormularioMarketing.ControlFormularioMarketing;
import mx.uam.ayd.proyecto.presentacion.Eventos.ControlEventos;
import mx.uam.ayd.proyecto.presentacion.HU_06.ControladorOrdenesPendientes;
import mx.uam.ayd.proyecto.presentacion.HU_07.ControladorEscaner;

@Component
public class controladorPrincipal {

    private final ServicioCliente servicioCliente;
    private final ServicioOrdenDeCompra servicioOrden;
    private final ControladorEscaner controladorEscaner;
    private final controladorCatalogoMezicuil controlTiendaLinea;
    private final vistaPrincipal ventanaPrincipal;
    private final ControlFormularioMarketing controlFormularioMarketing;
    private final ControlEventos controlEventos;
    private final ControladorOrdenesPendientes controladorOrdenes;

    private Cliente sesionActiva;

    @Autowired
    public controladorPrincipal(controladorCatalogoMezicuil controlTiendaLinea, vistaPrincipal ventanPrincipal,
            ServicioCliente servicioCliente, ControlFormularioMarketing controlFormularioMarketing,
            ControlEventos controlEventos, ControladorOrdenesPendientes controladorOrdenes,
            ServicioOrdenDeCompra servicioOrden, ControladorEscaner controladorEscaner) {
        this.controlTiendaLinea = controlTiendaLinea;
        this.ventanaPrincipal = ventanPrincipal;
        this.servicioCliente = servicioCliente;
        this.controlFormularioMarketing = controlFormularioMarketing;
        this.controlEventos = controlEventos;
        this.controladorOrdenes = controladorOrdenes;
        this.servicioOrden = servicioOrden;
        this.controladorEscaner = controladorEscaner;
    }

    // inicializamos la ventana
    @PostConstruct
    public void inicializar() {
        ventanaPrincipal.setControlPrincipal(this);
    }

    public void inicia() {
        ventanaPrincipal.muestra(null);
    }

    public void visitaTiendaLinea() {
        controlTiendaLinea.inicia();
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


    public void buscaCliente(String Nombre) {

        sesionActiva = servicioCliente.dameCliente(Nombre);
        if (sesionActiva != null) {
            // establecemos el id del usuario en el gestionCliente
            gestionCliente.getInstance().iniciarSesion(sesionActiva.getidCliente());
            System.out.println(
                    "EL id del usuaario que ingreso al sistema es: " + gestionCliente.getInstance().getIdActivo());
            ventanaPrincipal.muestra(sesionActiva.getNombre());
        } else {
            ventanaPrincipal.mostrarMensaje("Ingresa un usuario registrado.");
            ventanaPrincipal.muestra(null);
        }
    }
}