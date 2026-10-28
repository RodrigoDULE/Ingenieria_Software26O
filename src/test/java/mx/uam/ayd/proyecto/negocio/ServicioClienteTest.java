package mx.uam.ayd.proyecto.negocio;

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

import mx.uam.ayd.proyecto.datos.repositorioCliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;

@ExtendWith(MockitoExtension.class)
class ServicioClienteTest {

    @Mock
    private repositorioCliente repoCliente;

    @InjectMocks
    private ServicioCliente servicioCliente;

    @Test
    @DisplayName("dameCliente regresa el cliente cuando las credenciales son correctas")
    void dameCliente_credencialesCorrectas_regresaCliente() {
        Cliente cliente = org.mockito.Mockito.mock(Cliente.class);
        when(repoCliente.findByNombreAndContraseña("juan", "1234")).thenReturn(cliente);

        Cliente resultado = servicioCliente.dameCliente("juan", "1234");

        assertSame(cliente, resultado);
        // El servicio consulta el repositorio dos veces cuando encuentra al cliente
        verify(repoCliente, times(2)).findByNombreAndContraseña("juan", "1234");
    }

    @Test
    @DisplayName("dameCliente regresa null cuando las credenciales son incorrectas")
    void dameCliente_credencialesIncorrectas_regresaNull() {
        when(repoCliente.findByNombreAndContraseña("juan", "mala")).thenReturn(null);

        Cliente resultado = servicioCliente.dameCliente("juan", "mala");

        assertNull(resultado);
        verify(repoCliente, times(1)).findByNombreAndContraseña("juan", "mala");
    }

    @Test
    @DisplayName("dameCliente con parametros nulos regresa null")
    void dameCliente_parametrosNulos_regresaNull() {
        when(repoCliente.findByNombreAndContraseña(null, null)).thenReturn(null);

        assertNull(servicioCliente.dameCliente(null, null));
        verify(repoCliente).findByNombreAndContraseña(null, null);
    }
}