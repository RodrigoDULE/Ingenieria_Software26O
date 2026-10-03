package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.RepositorioOrdenDeCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra.EstadoOrden;

// inimos JUnit con mock
@ExtendWith(MockitoExtension.class)
public class Test_ServicioOrdenCompra {

    // el sustituto
    @Mock
    private RepositorioOrdenDeCompra repoOrden;

    // 2. Nuestro protagonista REAL (El Servicio que vamos a probar)
    @InjectMocks
    private ServicioOrdenDeCompra servicioOrden;

    @Test
    void testObtenerDetallesDeOrden() {
        
        // Caso 1. Cuando la orden existe
        
        // Given 
        Long idBuscado = 3L;
        OrdenDeCompra ordenSimulada = new OrdenDeCompra();
        ordenSimulada.setEstadoOrden(OrdenDeCompra.EstadoOrden.ENVIADA);
        
        // Como findById devuelve un Optional, lo envolvemos en Optional.of()
        when(repoOrden.findById(idBuscado)).thenReturn(Optional.of(ordenSimulada));

        // When (Ejecución)
        OrdenDeCompra resultado = servicioOrden.obtenerDetallesDeOrden(idBuscado);

        // Then (Verificación)
        assertNotNull(resultado, "La orden no debería ser nula");
        assertEquals(OrdenDeCompra.EstadoOrden.ENVIADA, resultado.getEstadoOrden());


        /*
        El caso en donde queremos recuperar todas las órdenes con estado REVISION_PENDIENTE
        es completamente análogo a este primer caso
        */

        // Caso 2. Cuando la orden (su id) no existe o ya fue recibida
        
        // Given
        Long idInexistente = 99L;
        // cuando se busque 99L, devolverá vacío
        when(repoOrden.findById(idInexistente)).thenReturn(Optional.empty());

        // When
        OrdenDeCompra resultadoNulo = servicioOrden.obtenerDetallesDeOrden(idInexistente);

        // Then
        assertNull(resultadoNulo, "La orden debería ser nula porque no existe");
    }



    // prueba para el cambio de estado de REVISION_PENDIENTE a AUTORIZADOTA osi
    @Test
    void testAutorizarOrden_Exito() {
        // given:  la orden existe y está pendiente
        Long idOrden = 1L;
        OrdenDeCompra ordenSimulada = new OrdenDeCompra();
        ordenSimulada.setEstadoOrden(EstadoOrden.REVISION_PENDIENTE);
        
        // cuando el método busque la orden, le damos nuestra orden simulada
        when(repoOrden.findById(idOrden)).thenReturn(Optional.of(ordenSimulada));
        
        // y cuando intente guardar, le decimos al mockito que simplemente devuelva la misma orden
        // Usamos any() de Mockito para decirle "acepta cualquier OrdenDeCompra que te manden a guardar"
        when(repoOrden.save(Mockito.any(OrdenDeCompra.class))).thenReturn(ordenSimulada);

        // when
        OrdenDeCompra resultado = servicioOrden.autorizarOrden(idOrden);

        // then, verificamos que los cambios sucedieron
        assertNotNull(resultado);
        // Comprobamos que si haya cambiado
        assertEquals(EstadoOrden.AUTORIZADA, resultado.getEstadoOrden());
        // Comprobamos que sí se le asignó una fecha de envío
        assertNotNull(resultado.getFechaEnvio());
    }

    @Test
    void testConfirmarRecepcion_FallaPorEstadoIncorrecto() {
        // giben: Escenario donde la orden existe, pero no está en estado AUTORIZADA
        Long idOrden = 2L;
        OrdenDeCompra ordenMalEstado = new OrdenDeCompra();
        // Puede ser RECIBIDA (ya se trabajó) o hasta PENDIENTE_REVISION (aun no se ha enviado)
        ordenMalEstado.setEstadoOrden(EstadoOrden.RECIBIDA);
        
        when(repoOrden.findById(idOrden)).thenReturn(Optional.of(ordenMalEstado));


        // when
        OrdenDeCompra resultado = servicioOrden.confirmarRecepcion(idOrden);

        // then: debería devolver null porque no pasó la validación del estado
        assertNull(resultado, "El resultado debe ser null si la orden no tenía el estado correcto");
        
    }
}