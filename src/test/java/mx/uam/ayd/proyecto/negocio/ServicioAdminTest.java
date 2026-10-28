package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.repositorioAdmin;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Administrador;

@ExtendWith(MockitoExtension.class)
class ServicioAdminTest {

    @Mock
    private repositorioAdmin repoAdmin;

    @InjectMocks
    private ServicioAdmin servicioAdmin;

    @Test
    @DisplayName("dameAdmin regresa el administrador cuando las credenciales son correctas")
    void dameAdmin_credencialesCorrectas_regresaAdministrador() {
        Administrador admin = org.mockito.Mockito.mock(Administrador.class);
        when(repoAdmin.findByNombreAndContraseña("admin", "1234")).thenReturn(admin);

        Administrador resultado = servicioAdmin.dameAdmin("admin", "1234");

        assertSame(admin, resultado);
        // El servicio consulta el repositorio dos veces cuando encuentra al administrador
        verify(repoAdmin, times(2)).findByNombreAndContraseña("admin", "1234");
    }

    @Test
    @DisplayName("dameAdmin regresa null cuando las credenciales son incorrectas")
    void dameAdmin_credencialesIncorrectas_regresaNull() {
        when(repoAdmin.findByNombreAndContraseña("admin", "mala")).thenReturn(null);

        Administrador resultado = servicioAdmin.dameAdmin("admin", "mala");

        assertNull(resultado);
        verify(repoAdmin, times(1)).findByNombreAndContraseña("admin", "mala");
    }

    @Test
    @DisplayName("dameAdmin con parametros nulos regresa null")
    void dameAdmin_parametrosNulos_regresaNull() {
        when(repoAdmin.findByNombreAndContraseña(null, null)).thenReturn(null);

        assertNull(servicioAdmin.dameAdmin(null, null));
        verify(repoAdmin).findByNombreAndContraseña(null, null);
    }

    @Test
    @DisplayName("dameAdmin entrega al repositorio exactamente los parametros recibidos")
    void dameAdmin_pasaParametrosAlRepositorio() {
        Administrador admin = org.mockito.Mockito.mock(Administrador.class);
        when(repoAdmin.findByNombreAndContraseña("maria", "abc")).thenReturn(admin);

        Administrador resultado = servicioAdmin.dameAdmin("maria", "abc");

        assertEquals(admin, resultado);
        verify(repoAdmin, times(2)).findByNombreAndContraseña("maria", "abc");
    }
}