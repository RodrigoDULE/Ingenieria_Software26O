package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.conffigPD.gestionCliente;
import mx.uam.ayd.proyecto.datos.repositorioCliente;
import mx.uam.ayd.proyecto.datos.repositorioIntermedioCarrito;
import mx.uam.ayd.proyecto.datos.repositorioProducto;
import mx.uam.ayd.proyecto.datos.repositoriocarritoCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.IntermediaCarritoProd;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.carritoCompra;

@ExtendWith(MockitoExtension.class)
public class servicioCarritoCompraTest {

    @Mock
    private repositoriocarritoCompra repoCarrito;
    @Mock
    private repositorioCliente repoCliente;
    @Mock
    private repositorioProducto repositorioProd;
    @Mock 
    private repositorioIntermedioCarrito repoInter;

    @InjectMocks
    private servicioCarritoCompra servicioCarritoCompra;

    @Test
    void testagregarItem() {
        // caso 1: retorna falso si es que no se pudo agregar el producto

        //given
        long idUsuario = 1L;
        gestionCliente.getInstance().iniciarSesion(idUsuario);
        int cantidad = 2; // La cantidad que intentamos agregar
        Producto productoMock = Mockito.mock(Producto.class);

        // Configuramos el comportamiento: simulamos que NO hay stock (<= 0)
        Mockito.when(productoMock.getcantidadStock()).thenReturn(0);

        // 2. EJECUCIÓN (Act)
        // Llamamos al método real de nuestro servicio inyectado
        boolean resultado = servicioCarritoCompra.agregarItem(productoMock, cantidad);

        // 3. VERIFICACIÓN (Assert)
        // Verificamos que el resultado sea efectivamente 'false'
        assertFalse(resultado);

        // caso 2: retorna verdadero si es que se agrego el producto exitosamente
        Cliente clienteFalso = new Cliente();
        // recuperamos el cliente
        when(repoCliente.findByIdCliente(idUsuario)).thenReturn(clienteFalso);
        // configiramos el comportamiento simulando que hay mas productos
        when(productoMock.getcantidadStock()).thenReturn(5);

        boolean resultado2 = servicioCarritoCompra.agregarItem(productoMock, cantidad);
        assertTrue(resultado2);
    }

    @Test
    void testrecuperaProductoEnCarrito() {
        // Caso 1: recupera el carrito con productos dentro
        long idActivo = 1L;
        Cliente cliente_falso = new Cliente();
        // regresa un cliente falso
        when(repoCliente.findByIdCliente(idActivo)).thenReturn(cliente_falso);
        gestionCliente.getInstance().iniciarSesion(idActivo);
        carritoCompra miCarrito = new carritoCompra();
        Producto productoFalso = new Producto(); // o un mock de producto
        // Le agregamos el producto al carrito
        miCarrito.setProducto(productoFalso);
        // Y le asignamos el carrito al cliente
        cliente_falso.setcarritoCompra(miCarrito);
        carritoCompra car = servicioCarritoCompra.recuperaProductoEnCarrito();
        assertNotNull(car, "El carrito devuelto no debería ser null");


        // caso 2: se retorna Null
        Cliente Falso2 = new Cliente();
        when(repoCliente.findByIdCliente(idActivo)).thenReturn(Falso2);
        carritoCompra res = servicioCarritoCompra.recuperaProductoEnCarrito(); 
        assertNull(res);

    }


    @Test
    void testEliminarProdCarrito(){
        /*
        //given
        long idActivo = 1L;
        gestionCliente.getInstance().iniciarSesion(idActivo);
        Cliente clFalso = new Cliente();
        carritoCompra car = new carritoCompra();
        Producto prod = new Producto();
        List<IntermediaCarritoProd> inter;
        
        car.setProducto(prod);
        clFalso.setcarritoCompra(car);
        
        when(repoCliente.findByIdCliente(idActivo)).thenReturn(clFalso); //Se activa el cliente
        when(repoInter.findByCarIdCarrito(idActivo)).thenReturn(inter);
        
        //when 
        boolean res = servicioCarritoCompra.EliminarProdCarrito(prod);
        
        //then
        assertTrue(res);
        */

        //**Generado por IA **/
        long idActivo = 1L;
        gestionCliente.getInstance().iniciarSesion(idActivo);

        Cliente clFalso = new Cliente();
        carritoCompra car = new carritoCompra();
        Producto prod = new Producto();
        IntermediaCarritoProd inter = new IntermediaCarritoProd();

        car.setIdCarrito(10L);
        inter.setCarrito(car);
        inter.setProd(prod);
        inter.setCantidadTotalProd(1);

        car.setProducto(prod);
        clFalso.setcarritoCompra(car);

        when(repoCliente.findByIdCliente(idActivo)).thenReturn(clFalso);
        when(repoInter.findByCarIdCarrito(car.getid())).thenReturn(List.of(inter));

        boolean res = servicioCarritoCompra.EliminarProdCarrito(prod);

        assertTrue(res);
    }

    @Test
    void testRecuperaTotalCalculadoPorCliente() {
        // given
        long idCliente = 1L;

        Cliente clienteFalso = new Cliente();
        carritoCompra carrito = new carritoCompra();
        carrito.setTotalCalculado(500.0f);
        clienteFalso.setcarritoCompra(carrito);

        when(repoCliente.findByIdCliente(idCliente)).thenReturn(clienteFalso);

        // when
        double resultado = servicioCarritoCompra.recuperaTotalCalculadoPorCliente(idCliente);

        // then
        assertEquals(500.0, resultado, 0.0001);
    }

    @Test
    void testRecuperaTotalCalculadoPorClienteSinCarrito() {
        // given
        long idCliente = 1L;

        Cliente clienteFalso = new Cliente();

        when(repoCliente.findByIdCliente(idCliente)).thenReturn(clienteFalso);

        // when
        double resultado = servicioCarritoCompra.recuperaTotalCalculadoPorCliente(idCliente);

        // then
        assertEquals(0.0, resultado, 0.0001);
    }
}

