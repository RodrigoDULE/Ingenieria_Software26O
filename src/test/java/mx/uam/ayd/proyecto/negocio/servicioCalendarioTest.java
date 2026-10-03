package mx.uam.ayd.proyecto.negocio;
//leo_d

import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;

import mx.uam.ayd.proyecto.datos.RepositorioEvento;
import mx.uam.ayd.proyecto.datos.RepositorioEmpleado;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Evento;



@ExtendWith(MockitoExtension.class)
public class servicioCalendarioTest {
    @Mock
    private RepositorioEvento repoEvento;

    @Mock 
    private RepositorioEmpleado repoEmpleado;

    @InjectMocks
    private servicioCalendario servicioCalendario;


    @Test
    void testRecuperaIdEvento(){
        //given
        long idEvento= 1L;

        Evento eventoMock= new Evento();
        eventoMock.setIdEvento(idEvento);

        when(repoEvento.findByIdEvento(idEvento)).thenReturn(eventoMock);
        Long resultado = servicioCalendario.reucuperaIdEvento(idEvento);

        // prueba valida si:
        assertNotNull(resultado);
        assertEquals(idEvento, resultado);
        verify(repoEvento).findByIdEvento(idEvento); //saber si consulto la base de datos


        
    }

   //consultado con ia
   @Test
    void testRecuperaEventoporFecha() {
    
        // G    iven (Preparación)
        LocalDate fechaBusqueda = LocalDate.of(2026, 8, 30);
    
        Evento evento1 = new Evento();
        evento1.setIdEvento(1L);
        evento1.setFechaE(fechaBusqueda);

        List<Evento> listaEsperada = new ArrayList<>();
        listaEsperada.add(evento1);

        // Enseñamos a Mockito: cuando busquen por esa fecha, regresa la lista con 1 evento
        when(repoEvento.findByFechaE(fechaBusqueda)).thenReturn(listaEsperada);

        // When (Ejecución)
        List<Evento> resultado = servicioCalendario.recuperaEventoporFecha(fechaBusqueda);

         
        assertNotNull(resultado, "La lista no debería ser nula");
        assertFalse(resultado.isEmpty(), "La lista no debería estar vacía");//pasar prueba
        assertEquals(1, resultado.size(), "Debería haber 1 evento en la lista");
        assertEquals(fechaBusqueda, resultado.get(0).getFechaE());


        // caso 2: retorna lista vacía si no hay eventos en esa fecha
    
        // given
        LocalDate fechaSinEventos = LocalDate.of(2026, 12, 31);
        when(repoEvento.findByFechaE(fechaSinEventos)).thenReturn(new ArrayList<>());

        // when
        List<Evento> resultadoVacio = servicioCalendario.recuperaEventoporFecha(fechaSinEventos);

        // then
        assertNotNull(resultadoVacio);
        assertTrue(resultadoVacio.isEmpty(), "La lista debería estar vacía");
    }
   
   @Test
    void testRecuperaPorNotificacion() {
        // caso 1: recupera exitosamente un evento por fecha de notificación
    
        // given 
        LocalDate fechaNotif = LocalDate.of(2026, 8, 15);
        Evento eventoEsperado = new Evento();
        eventoEsperado.setIdEvento(10L);
        eventoEsperado.setNotificacion(fechaNotif);

        when(repoEvento.findByNotificacion(fechaNotif)).thenReturn(eventoEsperado);

        // when (Ejecución)
        Evento resultado = servicioCalendario.recuperaPorNotificacion(fechaNotif);

        // then (Verificación)
        assertNotNull(resultado, "El evento no debería ser nulo");
        assertEquals(fechaNotif, resultado.getNotificacion());


        // caso 2: Retorna null cuando no existe evento con esa fecha de notificación
    
       
        LocalDate fechaSinNotif = LocalDate.of(2026, 1, 1);
        when(repoEvento.findByNotificacion(fechaSinNotif)).thenReturn(null);

        // when
        Evento resultadoNull = servicioCalendario.recuperaPorNotificacion(fechaSinNotif);

        // rhen
        assertNull(resultadoNull, "Debería retornar null al no encontrar registros");
    }

    @Test
    void testCalcularRepeticionDias(){
        LocalDate fechaEvento = LocalDate.of(2026, 8, 20);
        LocalDate fechaNotificacion= LocalDate.of(2026, 8, 1);
        int numDias= 5;

        List<LocalDate> resultado = servicioCalendario.calcularRepeticionDias(fechaEvento, fechaNotificacion, numDias);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertTrue(resultado.contains(fechaNotificacion)); //para pasar pruebas :P
        assertTrue(resultado.contains(fechaNotificacion.plusDays(5)));
        assertTrue(resultado.contains(fechaNotificacion.plusDays(10))); 
        assertThrows(IllegalArgumentException.class, () -> {
        servicioCalendario.calcularRepeticionDias(fechaEvento, fechaNotificacion, 0);
        });


    }


    @Test
    void testCalcularRepeticionSemanas() {
        // CASO 1: Camino feliz
        LocalDate fechaEvento = LocalDate.of(2026, 8, 30);
        LocalDate fechaNotificacion = LocalDate.of(2026, 8, 1);
        int numSemanas = 1;

        List<LocalDate> resultado = servicioCalendario.calcularRepeticionSemanas(fechaEvento, fechaNotificacion, numSemanas);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertTrue(resultado.contains(fechaNotificacion));
        assertTrue(resultado.contains(fechaNotificacion.plusWeeks(1)));//para cumplir la prueba

        // excepción cuando numsemanas <= 0
        assertThrows(IllegalArgumentException.class, () -> {
        servicioCalendario.calcularRepeticionSemanas(fechaEvento, fechaNotificacion, 0);
        });
    }

    @Test
    void testAdministrarNotificaciones_Exito() {
        // 1. ARRANGE (Preparación de datos)
        LocalDate fechaEvento = LocalDate.of(2026, 8, 20);
       LocalDate nuevaNotificacion = LocalDate.of(2026, 8, 1); // Anterior al evento -> VÁLIDO

        Evento eventoPrueba = new Evento();
        eventoPrueba.setFechaE(fechaEvento);

        // Entrenamos al mock: Cuando guarde cualquier evento, que regrese ese mismo evento
        when(repoEvento.save(any(Evento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 2. ACT (Ejecución del método)
        int numDias = 5;
        int numSemanas = 0;
        Evento resultado = servicioCalendario.administrarNotificaciones(eventoPrueba, numDias, numSemanas, nuevaNotificacion);

        // 3. ASSERT (Verificaciones)
        assertNotNull(resultado);
        assertEquals(nuevaNotificacion, resultado.getNotificacion());
    
        // Verificamos que sí se haya llamado al método save del repositorio exactamente 1 vez
       verify(repoEvento, times(1)).save(eventoPrueba);
    }

    @Test
    void testAdministrarNotificaciones_ExcepcionNotificacionPosterior() {
        // 1. ARRANGE
        LocalDate fechaEvento = LocalDate.of(2026, 8, 20);
        LocalDate notificacionInvalida = LocalDate.of(2026, 8, 25); // Posterior al evento -> INVÁLIDO

        Evento eventoPrueba = new Evento();
        eventoPrueba.setFechaE(fechaEvento);

        // 2. ACT & ASSERT
        // verificamos que lance la excepción
        assertThrows(IllegalArgumentException.class, () -> {
        servicioCalendario.administrarNotificaciones(eventoPrueba, 5, 0, notificacionInvalida);
        });

        // Verificamos que NUNCA intentó guardar en la base de datos porque la validación falló
        verify(repoEvento, never()).save(any(Evento.class));
    }




}