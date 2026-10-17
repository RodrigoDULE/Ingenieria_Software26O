package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.conffigPD.gestionCliente;
import mx.uam.ayd.proyecto.datos.RepositorioDetallesCompra;
import mx.uam.ayd.proyecto.datos.repositorioCliente;
import mx.uam.ayd.proyecto.datos.repositorioCompra;
import mx.uam.ayd.proyecto.datos.repositorioProducto;
import mx.uam.ayd.proyecto.datos.repositoriocarritoCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.IntermediaCarritoProd;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.carritoCompra;

@ExtendWith(MockitoExtension.class)
public class ServicioPagoTest {

    @Mock 
    private repositorioCliente repositorioCliente;
    @Mock 
    private repositorioCompra repositorioCompra;
    @Mock 
    private RepositorioDetallesCompra repositorioDetallesCompra;
    @Mock 
    private repositoriocarritoCompra repositoriocarritoCompra;
    @Mock 
    private repositorioProducto repositorioProducto;

    @InjectMocks 
    private ServicioPago servicioPago;

    //Criterio 2, cliente completa el pago
    @Test 
    void CompraVaciarCarrito(){
        //soy un cliente que tiene carrito con productos dentro de la tienda
        Cliente falso = new Cliente("Rodrigo", 20);
        IntermediaCarritoProd intermedia = new IntermediaCarritoProd();
        carritoCompra carFalso = new carritoCompra();

        falso.setidCliente(1L);
        gestionCliente.getInstance().iniciarSesion(falso.getidCliente());
        Producto prodFalso = new Producto("Mezcal", 25, "null", 25, 750, 25, "Mezcal", "null");
        intermedia.setProd(prodFalso);

        intermedia.setCarrito(carFalso);
        carFalso.setInterCar(new ArrayList<>(List.of(intermedia)));
        carFalso.setProducto(prodFalso);
        falso.setcarritoCompra(carFalso);

        //utilizamos base de datos
        when(repositorioCliente.findByIdCliente(gestionCliente.getInstance().getIdActivo())).thenReturn(falso);
    
        boolean funciona = servicioPago.peticionPagarCarrito(carFalso);

        assertTrue(funciona);
    }

    @Test
    void CompraCarritoVacio(){
    //soy un cliente que tiene carrito con productos dentro de la tienda
        Cliente falso = new Cliente("Rodrigo", 20);
        IntermediaCarritoProd intermedia = new IntermediaCarritoProd();
        carritoCompra carFalso = new carritoCompra();

        falso.setidCliente(1L);
        gestionCliente.getInstance().iniciarSesion(falso.getidCliente());

        intermedia.setCarrito(carFalso);
        carFalso.setInterCar(new ArrayList<>(List.of(intermedia)));
        falso.setcarritoCompra(carFalso);

        boolean funciona = servicioPago.peticionPagarCarrito(carFalso);

        assertFalse(funciona);
    }

    @Test 
    void CompraProductoNoDisponible(){
        Cliente falso = new Cliente("Rodrigo", 20);
        IntermediaCarritoProd intermedia = new IntermediaCarritoProd();
        carritoCompra carFalso = new carritoCompra();

        falso.setidCliente(1L);
        gestionCliente.getInstance().iniciarSesion(falso.getidCliente());
        Producto prodFalso = new Producto("Mezcal", 25, "null", 25, 750, 25, "Mezcal", "null");
        intermedia.setProd(prodFalso);

        intermedia.setCarrito(carFalso);
        carFalso.setInterCar(new ArrayList<>(List.of(intermedia)));
        carFalso.setProducto(prodFalso);
        falso.setcarritoCompra(carFalso);

        boolean Funciona = servicioPago.peticionPagarCarrito(carFalso);

        assertFalse(Funciona);
    }

    @Test 
    void COmpraUsuarioNulo(){
        IntermediaCarritoProd intermedia = new IntermediaCarritoProd();
        carritoCompra carFalso = new carritoCompra();

        Producto prodFalso = new Producto("Mezcal", 25, "null", 25, 750, 25, "Mezcal", "null");
        intermedia.setProd(prodFalso);

        intermedia.setCarrito(carFalso);
        carFalso.setInterCar(new ArrayList<>(List.of(intermedia)));
        carFalso.setProducto(prodFalso);

        boolean Funciona = servicioPago.peticionPagarCarrito(carFalso);

        assertFalse(Funciona);
    }
}
