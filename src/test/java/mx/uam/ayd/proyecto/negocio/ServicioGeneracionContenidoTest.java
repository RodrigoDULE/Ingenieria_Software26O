package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.negocio.EntidadNegocio.FormularioMarketing;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.FormularioMarketing.TipoContenido;
import mx.uam.ayd.proyecto.negocio.ServicioGeneracionContenido.VariacionContenido;

@ExtendWith(MockitoExtension.class)
public class ServicioGeneracionContenidoTest {

    @InjectMocks
    private ServicioGeneracionContenido servicioGeneracionContenido;

    @Test
    void testGenerarVariaciones_SoloTexto() {
        // given (Dado un formulario configurado para generar SOLO_TEXTO con 2 variaciones)
        FormularioMarketing formularioMock = mock(FormularioMarketing.class);
        when(formularioMock.getCantidadVariaciones()).thenReturn(2);
        when(formularioMock.getTipoContenido()).thenReturn(TipoContenido.SOLO_TEXTO);

        // when (Cuando generamos las variaciones)
        List<VariacionContenido> resultado = servicioGeneracionContenido.generarVariaciones(formularioMock);

        // then (Entonces se devuelve una lista de 2 elementos con copys de marketing)
        assertNotNull(resultado);
        assertEquals(2, resultado.size(), "Debe generar exactamente 2 variaciones");
        
        // Verificamos el formato para la primera variación (A)
        assertTrue(resultado.get(0).getDescripcion().contains("VARIACIÓN A"));
        assertTrue(resultado.get(0).getNombre().contains("Elixir ancestral")); // Valida que usó el primer copy del arreglo
    }

    @Test
    void testGenerarVariaciones_ImagenEstatica() {
        // given (Dado un formulario configurado para generar IMAGEN_ESTATICA con 3 variaciones)
        FormularioMarketing formularioMock = mock(FormularioMarketing.class);
        when(formularioMock.getCantidadVariaciones()).thenReturn(3);
        // Asumimos que existe IMAGEN_ESTATICA u otro que no sea SOLO_TEXTO
        when(formularioMock.getTipoContenido()).thenReturn(TipoContenido.IMAGEN_ESTATICA);

        // when
        List<VariacionContenido> resultado = servicioGeneracionContenido.generarVariaciones(formularioMock);

        // then (Entonces los nombres de las variaciones toman el formato genérico)
        assertNotNull(resultado);
        assertEquals(3, resultado.size(), "Debe generar exactamente 3 variaciones");
        
        // Verificamos que se asignaron las letras A, B y C correctamente
        assertEquals("Variación A", resultado.get(0).getNombre());
        assertEquals("Variación B", resultado.get(1).getNombre());
        assertEquals("Variación C", resultado.get(2).getNombre());
    }
}