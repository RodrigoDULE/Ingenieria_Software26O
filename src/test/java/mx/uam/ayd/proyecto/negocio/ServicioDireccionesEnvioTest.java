package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.RepositorioDirecciones;
import mx.uam.ayd.proyecto.datos.repositorioCliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.DireccionEnvio;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.DireccionEnvio.DatosDireccion;

@ExtendWith(MockitoExtension.class)
public class ServicioDireccionesEnvioTest {

    @Mock
    private RepositorioDirecciones repositorioDirecciones;

    @Mock
    private repositorioCliente repositorioCliente;

    @InjectMocks
    private ServicioDireccionesEnvio servicioDireccionesEnvio;

    @Test
    void testRegistrarDireccion() {
        // Criterio de Aceptación 1: Generación automatizada / Registro exitoso
        
        // given (Dado que el usuario configuró la información requerida)
        Cliente clienteMock = new Cliente();
        DatosDireccion datos = new DatosDireccion(
            "Juan Perez", "Av. Siempre Viva", "742", "Springfield", 
            "Ciudad", "Estado", "12345"
        );
        
        DireccionEnvio direccionGuardada = new DireccionEnvio(datos, clienteMock);
        
        // Simulamos que al guardar, el repositorio nos devuelve la entidad creada
        when(repositorioDirecciones.save(any(DireccionEnvio.class))).thenReturn(direccionGuardada);

        // when (Cuando inicie la generación / guardado)
        DireccionEnvio resultado = servicioDireccionesEnvio.registrarDireccion(datos, clienteMock);

        // then (Entonces el sistema procesa la información ingresada)
        assertNotNull(resultado, "La dirección registrada no debe ser nula");
        assertEquals("Juan Perez", resultado.getNombreCompleto());
        verify(repositorioDirecciones, times(1)).save(any(DireccionEnvio.class));
    }

    @Test
    void testMarcarComoPredeterminada() {
        // Criterio de Aceptación 4: Seleccionar dirección existente
        
        // --- CASO 1: Éxito (Se encuentra la dirección y se asigna) ---
        // given
        Long idDireccion = 1L;
        Cliente cliente = new Cliente();
        DireccionEnvio direccionEncontrada = new DireccionEnvio();
        
        when(repositorioDirecciones.findById(idDireccion)).thenReturn(Optional.of(direccionEncontrada));

        // when 
        boolean resultadoExito = servicioDireccionesEnvio.marcarComoPredeterminada(idDireccion, cliente);

        // then 
        assertTrue(resultadoExito);
        assertEquals(direccionEncontrada, cliente.getDireccionPredeterminada(), "La dirección debe asignarse al cliente");
        verify(repositorioCliente, times(1)).save(cliente); // Verifica que se guardó el cliente con su nueva configuración

        // --- CASO 2: Falla (No existe la dirección) ---
        // given
        Long idDireccionInexistente = 99L;
        when(repositorioDirecciones.findById(idDireccionInexistente)).thenReturn(Optional.empty());

        // when
        boolean resultadoFalla = servicioDireccionesEnvio.marcarComoPredeterminada(idDireccionInexistente, cliente);

        // then
        assertFalse(resultadoFalla, "Debe retornar falso si la dirección no se encuentra");
    }

    @Test
    void testEliminarDireccion() {
        // Criterio de Aceptación 3: Eliminar dirección (Baja lógica)
        
        // --- CASO 1: Éxito (Se aplica baja lógica) ---
        // given
        Long idDireccion = 2L;
        DireccionEnvio direccion = new DireccionEnvio();
        direccion.setActiva(true); // Inicialmente activa
        
        when(repositorioDirecciones.findById(idDireccion)).thenReturn(Optional.of(direccion));

        // when (Cuando el usuario intente eliminar)
        boolean resultadoExito = servicioDireccionesEnvio.eliminarDireccion(idDireccion);

        // then (Entonces se realiza la baja lógica)
        assertTrue(resultadoExito);
        assertFalse(direccion.isActiva(), "La dirección debe quedar inactiva (baja lógica)");
        verify(repositorioDirecciones, times(1)).save(direccion); // Verifica que el cambio se guardó en BD

        // --- CASO 2: Falla (No se encuentra la dirección a eliminar) ---
        // given
        Long idFalso = 50L;
        when(repositorioDirecciones.findById(idFalso)).thenReturn(Optional.empty());

        // when
        boolean resultadoFalla = servicioDireccionesEnvio.eliminarDireccion(idFalso);

        // then
        assertFalse(resultadoFalla, "Debe retornar falso si no encuentra la dirección a eliminar");
    }
}