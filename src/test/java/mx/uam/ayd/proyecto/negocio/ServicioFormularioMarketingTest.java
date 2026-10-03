package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.RepositorioFormularioMarketing;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.FormularioMarketing;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.FormularioMarketing.DatosFormulario;

@ExtendWith(MockitoExtension.class)
public class ServicioFormularioMarketingTest {

    @Mock
    private RepositorioFormularioMarketing repositorioFormularioMarketing;

    @InjectMocks
    private ServicioFormularioMarketing servicioFormularioMarketing;

    @Test
    void testRegistrarFormulario_ConArchivos() {
        // given (Dado que el usuario llenó los datos y subió archivos)
        DatosFormulario datosMock = mock(DatosFormulario.class);
        
        // Simulamos un archivo subido por el usuario
        File archivoMock = mock(File.class);
        when(archivoMock.getName()).thenReturn("logo_empresa.png");
        when(archivoMock.length()).thenReturn(2048L); // 2KB
        
        List<File> archivos = new ArrayList<>();
        archivos.add(archivoMock);

        FormularioMarketing formularioGuardado = new FormularioMarketing();
        // Simulamos el guardado en BD
        when(repositorioFormularioMarketing.save(any(FormularioMarketing.class))).thenReturn(formularioGuardado);

        // when (Cuando se solicita registrar el formulario)
        FormularioMarketing resultado = servicioFormularioMarketing.registrarFormulario(datosMock, archivos);

        // then (Entonces se procesa la metadata y se guarda exitosamente)
        assertNotNull(resultado, "El formulario guardado no debe ser nulo");
        verify(repositorioFormularioMarketing, times(1)).save(any(FormularioMarketing.class));
    }

    @Test
    void testRegistrarFormulario_SinArchivos() {
        // given (Dado que el usuario llena el formulario sin subir ningún archivo)
        DatosFormulario datosMock = mock(DatosFormulario.class);
        List<File> archivosNulos = null; // Caso donde la lista viene nula

        FormularioMarketing formularioGuardado = new FormularioMarketing();
        when(repositorioFormularioMarketing.save(any(FormularioMarketing.class))).thenReturn(formularioGuardado);

        // when 
        FormularioMarketing resultado = servicioFormularioMarketing.registrarFormulario(datosMock, archivosNulos);

        // then (Se guarda sin problemas, con una lista de ArchivosReferencia vacía)
        assertNotNull(resultado);
        verify(repositorioFormularioMarketing, times(1)).save(any(FormularioMarketing.class));
    }
}