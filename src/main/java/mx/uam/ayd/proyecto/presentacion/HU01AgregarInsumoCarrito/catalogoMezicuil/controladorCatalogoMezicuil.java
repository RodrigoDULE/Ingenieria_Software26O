package mx.uam.ayd.proyecto.presentacion.HU01AgregarInsumoCarrito.catalogoMezicuil;

import java.util.ArrayList;
import java.util.List;

//GEnerado por IA
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.conffigPD.GestionEscena;
import mx.uam.ayd.proyecto.conffigPD.gestionCliente;
import mx.uam.ayd.proyecto.negocio.ServicioFavorito;
import mx.uam.ayd.proyecto.negocio.servicioProducto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;
import mx.uam.ayd.proyecto.presentacion.HU01AgregarInsumoCarrito.DetallesProductoAgregarCarrito.controladorDetallesProductoAgregarCarrito;
import mx.uam.ayd.proyecto.presentacion.HU01AgregarInsumoCarrito.DetallesProductoAgregarCarrito.vistaDetallesProductoAgregarCarrito;
import mx.uam.ayd.proyecto.presentacion.SerBot.ControladorBot;
//GEnerado por IA
import mx.uam.ayd.proyecto.presentacion.VolverAlCatalogoEvent;

/**
 * controladorCatalogoMezicuil
 */

@Component
public class controladorCatalogoMezicuil {

    private final servicioProducto servicioProducto;
    private final vistaCatalogoMezicuil vistaCatalogo;
    private final vistaDetallesProductoAgregarCarrito vistaDetallesProducto;
    private final controladorDetallesProductoAgregarCarrito controlDetalles;

    private final ControladorBot ctrlBot;
    private final ServicioFavorito servicioFavorito;

    public controladorCatalogoMezicuil(servicioProducto servicioProducto, vistaCatalogoMezicuil vistaCatalogo, vistaDetallesProductoAgregarCarrito vistaDetallesProducto, controladorDetallesProductoAgregarCarrito controlDetalles, ControladorBot ctrl, ServicioFavorito servicioFavorito){
        this.controlDetalles = controlDetalles;
        this.vistaDetallesProducto = vistaDetallesProducto;
        this.servicioProducto = servicioProducto;
        this.vistaCatalogo = vistaCatalogo;
        this.ctrlBot = ctrl;
        this.servicioFavorito = servicioFavorito;
    } 


    //Inicializamos el controlador con la vista de catalogo, el postContruct sirve para ejecutar un metodo justo despues de crear el constructo e inyectar dependencias
    @PostConstruct
    public void inicializar(){
        vistaCatalogo.setControlador(this);
    }

    public void inicia(){
        List<Producto> prod = servicioProducto.obtenerProductosDisponibles();
        vistaCatalogo.muestra(prod);
    }

    //GEnerado por IA
    @EventListener
    //GEnerado por IA
    public void regresarAlCatalogo(VolverAlCatalogoEvent event){
        //GEnerado por IA
        inicia();
        //GEnerado por IA
    }

    public void validarCriterio(String criterio){
        List<Producto> prodCriterio = new ArrayList<Producto>();
        
        if("Todo".equals(criterio)){
            prodCriterio = servicioProducto.obtenerProductosDisponibles();
            vistaCatalogo.muestra(prodCriterio);
        }else if("Mezcal".equals(criterio) || "Complementos".equals(criterio)){
            prodCriterio = servicioProducto.obtenerProductoPorCriterio(criterio);
            System.out.println("Producto encontrados: ");
            for(Producto a: prodCriterio){
                System.out.println(a);
            }
            vistaCatalogo.muestra(prodCriterio);
        }else{
            //Aqui entra el caso de la barra de busqueda
            System.out.println("Dentro de barra busqueda: "+ criterio);
            prodCriterio=servicioProducto.obtenerPorBusquedaPersonalizada(criterio);

            if(!prodCriterio.isEmpty()){
                System.out.println("Tamaño de los porductos encontrados: "+ prodCriterio.size());
                vistaCatalogo.muestra(prodCriterio);
            }else{
                vistaCatalogo.mostrarMensaje("Lo sentimos, por el momento no hay existencia de este producto");
            }
        }
    }
 
    //redireccion a los detalles de producto
    public void detallesProductoSeleccionado(Producto p){
        System.out.println("el producto trabajando " + p);
        vistaDetallesProducto.muestraDetallesProd(p, GestionEscena.getInstEscena().getStage());
    }


    public void irCarrito(){
        controlDetalles.visitaCarritoCompra();
    }

    //  aquí obtiene los productos favoritos del cliente activo y los envía a la vista del catálogo
    public void mostrarFavoritos() {
        long idCliente = gestionCliente.getInstance().getIdActivo();
        List<Producto> productosFavoritos = servicioFavorito.obtenerProductosFavoritos(idCliente);
        vistaCatalogo.muestraFavoritos(productosFavoritos);
    }

    // consulta si el cliente activo ya guardó este producto como favorito
    public boolean esFavorito(Producto producto) {
        long idCliente = gestionCliente.getInstance().getIdActivo();
        return servicioFavorito.esFavorito(idCliente, producto.getidProducto());
    }

    // intenta guardar el cambio del corazon y devuelve "true" o "false" para avisar si lo logró o no
    public boolean cambiarEstadoFavorito(Producto producto, boolean yaEsFavorito) {
        long idCliente = gestionCliente.getInstance().getIdActivo();
        long idProducto = producto.getidProducto();

        return yaEsFavorito
                ? servicioFavorito.quitarFavorito(idCliente, idProducto)
                : servicioFavorito.agregarFavorito(idCliente, idProducto);
    }

    public void EnviarBot(){
        ctrlBot.muestra();
    }
}