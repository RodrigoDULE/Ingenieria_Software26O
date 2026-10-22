package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.RepositorioFavorito;
import mx.uam.ayd.proyecto.datos.repositorioCliente;
import mx.uam.ayd.proyecto.datos.repositorioProducto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Favorito;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;

@ExtendWith(MockitoExtension.class)
class ServicioFavoritoTest {

    private static final long ID_CLIENTE = 1L;
    private static final long ID_PRODUCTO = 10L;

    @Mock
    private RepositorioFavorito repositorioFavorito;

    @Mock
    private repositorioCliente repositorioCliente;

    @Mock
    private repositorioProducto repositorioProducto;

    @InjectMocks
    private ServicioFavorito servicioFavorito;

    // comprueba que se guarde la relación entre cliente y producto si aún no existe
    @Test
    void agregarFavoritoGuardaLaRelacionCuandoClienteYProductoExisten() {
        Cliente cliente = new Cliente();
        Producto producto = new Producto();
        when(repositorioCliente.findByIdCliente(ID_CLIENTE)).thenReturn(cliente);
        when(repositorioProducto.findByIdProducto(ID_PRODUCTO)).thenReturn(producto);
        when(repositorioFavorito
                .existsByClienteFK_IdClienteAndProductoFK_IdProducto(ID_CLIENTE, ID_PRODUCTO))
                .thenReturn(false);

        boolean resultado = servicioFavorito.agregarFavorito(ID_CLIENTE, ID_PRODUCTO);

        assertTrue(resultado);
        ArgumentCaptor<Favorito> favoritoGuardado = ArgumentCaptor.forClass(Favorito.class);
        verify(repositorioFavorito).save(favoritoGuardado.capture());
        assertSame(cliente, favoritoGuardado.getValue().getClienteFK());
        assertSame(producto, favoritoGuardado.getValue().getProductoFK());
    }

    // comprueba que no se intente guardar un favorito si el cliente no existe
    @Test
    void agregarFavoritoFallaCuandoElClienteNoExiste() {
        when(repositorioCliente.findByIdCliente(ID_CLIENTE)).thenReturn(null);
        when(repositorioProducto.findByIdProducto(ID_PRODUCTO)).thenReturn(new Producto());

        boolean resultado = servicioFavorito.agregarFavorito(ID_CLIENTE, ID_PRODUCTO);

        assertFalse(resultado);
        verify(repositorioFavorito, never()).save(any(Favorito.class));
    }

    // comprueba que no se intente guardar un favorito si el producto no existe
    @Test
    void agregarFavoritoFallaCuandoElProductoNoExiste() {
        when(repositorioCliente.findByIdCliente(ID_CLIENTE)).thenReturn(new Cliente());
        when(repositorioProducto.findByIdProducto(ID_PRODUCTO)).thenReturn(null);

        boolean resultado = servicioFavorito.agregarFavorito(ID_CLIENTE, ID_PRODUCTO);

        assertFalse(resultado);
        verify(repositorioFavorito, never()).save(any(Favorito.class));
    }

    // comprueba que el servicio evite duplicar la relación si ya era favorito
    @Test
    void agregarFavoritoFallaCuandoLaRelacionYaExiste() {
        when(repositorioCliente.findByIdCliente(ID_CLIENTE)).thenReturn(new Cliente());
        when(repositorioProducto.findByIdProducto(ID_PRODUCTO)).thenReturn(new Producto());
        when(repositorioFavorito
                .existsByClienteFK_IdClienteAndProductoFK_IdProducto(ID_CLIENTE, ID_PRODUCTO))
                .thenReturn(true);

        boolean resultado = servicioFavorito.agregarFavorito(ID_CLIENTE, ID_PRODUCTO);

        assertFalse(resultado);
        verify(repositorioFavorito, never()).save(any(Favorito.class));
    }

    // comprueba que al quitar un favorito existente se elimine sólo esa relación
    @Test
    void quitarFavoritoEliminaLaRelacionCuandoExiste() {
        Favorito favorito = new Favorito();
        when(repositorioCliente.findByIdCliente(ID_CLIENTE)).thenReturn(new Cliente());
        when(repositorioProducto.findByIdProducto(ID_PRODUCTO)).thenReturn(new Producto());
        when(repositorioFavorito
                .findByClienteFK_IdClienteAndProductoFK_IdProducto(ID_CLIENTE, ID_PRODUCTO))
                .thenReturn(favorito);

        boolean resultado = servicioFavorito.quitarFavorito(ID_CLIENTE, ID_PRODUCTO);

        assertTrue(resultado);
        verify(repositorioFavorito).delete(favorito);
    }

    // comprueba que quitar un producto no marcado no intente borrar ninguna relación
    @Test
    void quitarFavoritoFallaCuandoLaRelacionNoExiste() {
        when(repositorioCliente.findByIdCliente(ID_CLIENTE)).thenReturn(new Cliente());
        when(repositorioProducto.findByIdProducto(ID_PRODUCTO)).thenReturn(new Producto());
        when(repositorioFavorito
                .findByClienteFK_IdClienteAndProductoFK_IdProducto(ID_CLIENTE, ID_PRODUCTO))
                .thenReturn(null);

        boolean resultado = servicioFavorito.quitarFavorito(ID_CLIENTE, ID_PRODUCTO);

        assertFalse(resultado);
        verify(repositorioFavorito, never()).delete(any(Favorito.class));
    }

    // comprueba que no se intente quitar el favorito cuando falta el cliente o el producto
    @Test
    void quitarFavoritoFallaCuandoClienteOProductoNoExisten() {
        when(repositorioCliente.findByIdCliente(ID_CLIENTE)).thenReturn(null);
        when(repositorioProducto.findByIdProducto(ID_PRODUCTO)).thenReturn(new Producto());

        boolean resultado = servicioFavorito.quitarFavorito(ID_CLIENTE, ID_PRODUCTO);

        assertFalse(resultado);
        verify(repositorioFavorito, never())
                .findByClienteFK_IdClienteAndProductoFK_IdProducto(ID_CLIENTE, ID_PRODUCTO);
        verify(repositorioFavorito, never()).delete(any(Favorito.class));
    }

    // comprueba que la consulta de estado reporte el valor devuelto por el repositorio.
    @Test
    void esFavoritoDevuelveElEstadoConsultado() {
        when(repositorioFavorito
                .existsByClienteFK_IdClienteAndProductoFK_IdProducto(ID_CLIENTE, ID_PRODUCTO))
                .thenReturn(true);

        boolean resultado = servicioFavorito.esFavorito(ID_CLIENTE, ID_PRODUCTO);

        assertTrue(resultado);
        verify(repositorioFavorito)
                .existsByClienteFK_IdClienteAndProductoFK_IdProducto(ID_CLIENTE, ID_PRODUCTO);
    }

    // comprueba que el servicio extraiga los productos de las relaciones favoritas del cliente
    @Test
    void obtenerProductosFavoritosDevuelveLosProductosRelacionados() {
        Producto primerProducto = new Producto();
        Producto segundoProducto = new Producto();
        Favorito primerFavorito = new Favorito();
        Favorito segundoFavorito = new Favorito();
        primerFavorito.setProductoFK(primerProducto);
        segundoFavorito.setProductoFK(segundoProducto);
        when(repositorioFavorito.findByClienteFK_IdCliente(ID_CLIENTE))
                .thenReturn(List.of(primerFavorito, segundoFavorito));

        List<Producto> resultado = servicioFavorito.obtenerProductosFavoritos(ID_CLIENTE);

        assertEquals(2, resultado.size());
        assertSame(primerProducto, resultado.get(0));
        assertSame(segundoProducto, resultado.get(1));
    }

    // comprueba que un cliente sin favoritos reciba una lista vacía.
    @Test
    void obtenerProductosFavoritosDevuelveListaVaciaCuandoNoHayFavoritos() {
        when(repositorioFavorito.findByClienteFK_IdCliente(ID_CLIENTE)).thenReturn(List.of());

        List<Producto> resultado = servicioFavorito.obtenerProductosFavoritos(ID_CLIENTE);

        assertTrue(resultado.isEmpty());
    }
}
