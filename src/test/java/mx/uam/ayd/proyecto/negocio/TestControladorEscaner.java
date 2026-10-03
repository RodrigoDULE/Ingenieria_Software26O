package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.negocio.EntidadNegocio.DetalleOrden;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Insumo;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra;
import mx.uam.ayd.proyecto.presentacion.HU_07.*;

@ExtendWith(MockitoExtension.class)
public class TestControladorEscaner {

    // dependencias mocks
    @Mock private VistaEscaner vista;
    @Mock private ServicioOrdenDeCompra servicioOrden;
    @Mock private ControladorLoteCerradoExito controladorLote;

    // inyeccion
    @InjectMocks 
    private ControladorEscaner controlador;

    private OrdenDeCompra ordenPrueba;
    private DetalleOrden detallePrueba;

    // preparamos condiciones iniciales
    @BeforeEach
    void prepararDatos() {
        ordenPrueba = new OrdenDeCompra();
        ordenPrueba.setId(1L);
        
        Insumo insumo = new Insumo();
        insumo.setSku("SKU-123"); // El sku que vamos a escanear
        
        detallePrueba = new DetalleOrden();
        detallePrueba.setInsumo(insumo);
        detallePrueba.setCantidad(5); // es la cantidad esperada
        detallePrueba.setCantidadEscaneada(0); // Empezamos en 0
        
        ordenPrueba.agregarDetalle(detallePrueba);
    }

    @Test
    void testProcesarEscaneo_SumaCorrectamente() {
        // when: escaneamos un codigo correcto
        boolean exito = controlador.procesarEscaneo("SKU-123", ordenPrueba);

        // Then: debe devolver true y la cantidad escaneada debe subir a 1
        assertTrue(exito);
        assertEquals(1, detallePrueba.getCantidadEscaneada());
    }

    @Test
    void testProcesarEscaneo_FallaConSkuIncorrecto() {
        // When: escaneamos un código que no está en la orden
        boolean exito = controlador.procesarEscaneo("SKU-FALSO", ordenPrueba);

        // Then: debe devolver false y la cantidad se queda en 0
        assertFalse(exito);
        assertEquals(0, detallePrueba.getCantidadEscaneada());
    }

    @Test
    void testVerificarOrdenCompleta() {
        // given: simulamos que ya escaneamos 4 de 5
        detallePrueba.setCantidadEscaneada(4);
        
        // then: La orden aun no está completa
        assertFalse(controlador.verificarOrdenCompleta(ordenPrueba));

        // given: simulamos que ya escaneamos los 5
        detallePrueba.setCantidadEscaneada(5);

        // then: La orden ya está completa
        assertTrue(controlador.verificarOrdenCompleta(ordenPrueba));
    }

}