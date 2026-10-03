package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.RepositorioArchivoReferencia;
import mx.uam.ayd.proyecto.datos.RepositorioFormularioMarketing;
import mx.uam.ayd.proyecto.datos.RepositorioPublicacionMarketing;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.FormularioMarketing;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.PublicacionMarketing;

@ExtendWith(MockitoExtension.class)
public class ServicioGuardarContenidoTest {

    @Mock
    private RepositorioFormularioMarketing repoFormularioMarketing;

    @Mock
    private RepositorioArchivoReferencia repoArchivoReferencia;

    @Mock
    private RepositorioPublicacionMarketing repoPublicacion;

    @InjectMocks
    private ServicioGuardarContenido servicioGuardarContenido;

    @Test
    void testGuardarFormulario_Exito() {
        // given
        FormularioMarketing formulario = new FormularioMarketing();
        formulario.setNombre("Campaña de Verano");

        when(repoFormularioMarketing.save(any(FormularioMarketing.class))).thenReturn(formulario);

        // when
        FormularioMarketing resultado = servicioGuardarContenido.guardarFormulario(formulario);

        // then
        assertNotNull(resultado);
        assertEquals("Campaña de Verano", resultado.getNombre());// para pasar la prueba
        verify(repoFormularioMarketing, times(1)).save(formulario);
    }

    @Test
    void testGuardarFormulario_Excepciones() {
        // Caso 1: Formulario null
        assertThrows(IllegalArgumentException.class, () -> {
            servicioGuardarContenido.guardarFormulario(null);
        });

        // Caso 2: Nombre null
        FormularioMarketing formSinNombre = new FormularioMarketing();
        assertThrows(IllegalArgumentException.class, () -> {
            servicioGuardarContenido.guardarFormulario(formSinNombre);
        });

        // Caso 3: Nombre vacío
        FormularioMarketing formNombreVacio = new FormularioMarketing();
        formNombreVacio.setNombre("   ");
        assertThrows(IllegalArgumentException.class, () -> {
            servicioGuardarContenido.guardarFormulario(formNombreVacio);
        });

        // Verificamos que el repositorio nunca guardó nada debido a las excepciones
        verify(repoFormularioMarketing, never()).save(any(FormularioMarketing.class));
    }

    @Test
    void testObtenerFormularios() {
        // given
        List<FormularioMarketing> listaEsperada = new ArrayList<>();
        FormularioMarketing f1 = new FormularioMarketing();
        listaEsperada.add(f1);

        when(repoFormularioMarketing.findAll()).thenReturn(listaEsperada);

        // when
        List<FormularioMarketing> resultado = servicioGuardarContenido.obtenerFormularios();

        // then
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(repoFormularioMarketing, times(1)).findAll();
    }

    @Test
    void testBuscarFormulario() {
        // given
        long idFormulario = 10L;
        FormularioMarketing formularioMock = new FormularioMarketing();
        
        when(repoFormularioMarketing.findById(idFormulario)).thenReturn(formularioMock);

        // when
        FormularioMarketing resultado = servicioGuardarContenido.buscarFormulario(idFormulario);

        // then
        assertNotNull(resultado);
        verify(repoFormularioMarketing).findById(idFormulario);
    }

    @Test
    void testActualizarFormulario_Excepciones() {
        // Caso 1: Formulario null
        assertThrows(IllegalArgumentException.class, () -> {
            servicioGuardarContenido.actualizarFormulario(null);
        });

        // Caso 2: Nombre vacío (verifica la excepción específica)
        FormularioMarketing formInvalido = new FormularioMarketing();
        formInvalido.setNombre("");
        
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            servicioGuardarContenido.actualizarFormulario(formInvalido);
        });
        
        assertEquals("poner nombre :(", excepcion.getMessage());
        verify(repoFormularioMarketing, never()).save(any());
    }

    @Test
    void testPublicarFormulario_Exito() {
        // given
        long idFormulario = 1L;
        FormularioMarketing formularioMock = new FormularioMarketing();
        
        // Asignamos una lista de plataformas válida
        List<String> plataformas = new ArrayList<>();
        plataformas.add("Instagram Post");
        plataformas.add("Facebook");
        formularioMock.setPlataformasDestino(plataformas);
        
        PublicacionMarketing publicacionMock = new PublicacionMarketing();

        when(repoFormularioMarketing.findById(idFormulario)).thenReturn(formularioMock);

        // when
        PublicacionMarketing resultado = servicioGuardarContenido.publicarFormulario(idFormulario, publicacionMock);

        // then
        assertNotNull(resultado);
        assertEquals(formularioMock, resultado.getFormularioMarketing());
        assertEquals(publicacionMock, formularioMock.getPublicacion());
        verify(repoFormularioMarketing, times(1)).save(formularioMock);
    }

    
    //---------------------------------------------------------------------------------------
    
    @Test
    void testPublicarFormulario_Excepciones() {
        long idFormulario = 1L;
        PublicacionMarketing publicacionMock = new PublicacionMarketing();

        // Caso 1: Formulario no encontrado (findById regresa null)
        when(repoFormularioMarketing.findById(idFormulario)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> {
            servicioGuardarContenido.publicarFormulario(idFormulario, publicacionMock);
        });

        // Caso 2: Formulario ya tiene publicación asignada
        FormularioMarketing formConPublicacion = new FormularioMarketing();
        formConPublicacion.setPublicacion(new PublicacionMarketing());
        
        when(repoFormularioMarketing.findById(idFormulario)).thenReturn(formConPublicacion);
        
        assertThrows(IllegalArgumentException.class, () -> {
            servicioGuardarContenido.publicarFormulario(idFormulario, publicacionMock);
        });

        // Caso 3: Plataformas destino es null o vacía
        FormularioMarketing formSinPlataforma = new FormularioMarketing();
        // Al crear el objeto, la lista se inicializa vacía según tu entidad: plataformasDestino = new ArrayList<>()
        
        when(repoFormularioMarketing.findById(idFormulario)).thenReturn(formSinPlataforma);
        
        assertThrows(IllegalArgumentException.class, () -> {
            servicioGuardarContenido.publicarFormulario(idFormulario, publicacionMock);
        });
        
        // que no se haya modificado la base de datos en ninguno de los casos que fallaron
        verify(repoFormularioMarketing, never()).save(any());
    }
}