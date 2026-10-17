package mx.uam.ayd.proyecto.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import mx.uam.ayd.proyecto.BaseIntegrationTest;
import mx.uam.ayd.proyecto.conffigPD.gestionCliente;
import mx.uam.ayd.proyecto.datos.repositorioCliente;
import mx.uam.ayd.proyecto.datos.repositorioIntermedioCarrito;
import mx.uam.ayd.proyecto.datos.repositorioProducto;
import mx.uam.ayd.proyecto.datos.repositoriocarritoCompra;
import mx.uam.ayd.proyecto.negocio.servicioCarritoCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.IntermediaCarritoProd;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.carritoCompra;

public class CarritoIntegrationTest extends BaseIntegrationTest {
    
    @Autowired
    private repositorioCliente repoCliente;
    @Autowired
    private repositorioProducto repoProd;
    @Autowired
    private repositoriocarritoCompra repoCar;
    @Autowired
    private repositorioIntermedioCarrito repoInter;
    @Autowired
    private servicioCarritoCompra servicioCarrito;

    @Test
    @Transactional
    void TestAgregarProductoCarrito(){
        //given
        Producto producto = new Producto();
        Cliente cliente = new Cliente();
        producto.setcantidadStock(20);
        repoCliente.save(cliente);
        gestionCliente.getInstance().iniciarSesion(cliente.getidCliente());

        repoProd.save(producto);
        
        //when
        boolean productoAgregado = servicioCarrito.agregarItem(producto, 5);

        //then
        assertTrue(productoAgregado);
    }

    @Test
    @Transactional
    void TestRecuperaProductoCarrito(){
        //given
        Producto prd = new Producto();
        Cliente cliente = new Cliente();
        repoCliente.save(cliente);
        gestionCliente.getInstance().iniciarSesion(cliente.getidCliente());
        
        carritoCompra car = new carritoCompra();
        repoCar.save(car);
        repoProd.save(prd);
        cliente.setcarritoCompra(car);
        car.setProducto(prd);
        


        //When
        //recupera el carrito
        carritoCompra carrito = servicioCarrito.recuperaProductoEnCarrito(); 
    
        //then
        assertNotNull(carrito);

    }



    @Test
    @Transactional
    void testEliminarProducto(){
        //given
        Cliente client = new Cliente();

        Producto prod = new Producto();
        repoProd.save(prod);

        carritoCompra car = new carritoCompra();
        car.setProducto(prod);

        IntermediaCarritoProd inter = new IntermediaCarritoProd();
        inter.setCarrito(car);
        inter.setProd(prod);
        inter.setCantidadTotalProd(1);
        car.setInterCar(new ArrayList<>(List.of(inter)));
        car.setCantidadTotalCompra(1);
        client.setcarritoCompra(car);

        repoCar.save(car);
        repoCliente.save(client);
        gestionCliente.getInstance().iniciarSesion(client.getidCliente());

        //when
        boolean borrado = servicioCarrito.EliminarProdCarrito(prod);

        //then
        assertTrue(borrado);
        assertTrue(car.getProductos().isEmpty());
        assertTrue(car.getInterCar().isEmpty());
        assertTrue(repoInter.findByCarIdCarrito(car.getIdCarrito()).isEmpty());
    }
}
