package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.RepositorioEvento;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Empleado;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Evento;

@ExtendWith(MockitoExtension.class)
public class ServicioEventoTest {

    @Mock
    private RepositorioEvento repoEvento;

    @InjectMocks
    private ServicioEvento servicioEvento;

    @Test
    void testObtenerEventos() {
        // Caso 1: retorna una lista vacía si no hay eventos

        // given
        List<Evento> listaEventos = new ArrayList<>();
        when(repoEvento.findAll()).thenReturn(listaEventos);

        // when
        List<Evento> eventos = servicioEvento.obtenerEventos();

        // then
        assertEquals(0, eventos.size());
    }

    @Test
    void testAgregarEvento() {

        // Given
        List<Empleado> empleados = new ArrayList<>();
        Empleado empleado = new Empleado();
        empleados.add(empleado);

        // When
        boolean resultado = servicioEvento.agregarEvento(
            "Boda",
            "Social",
            LocalDate.of(2026, 8, 15),
            "10:00",
            "14:00",
            "Contado",
            "Salón",
            "",
            100,
            20,
            LocalDate.of(2026, 8, 8),
            empleados);


        // Then
        assertTrue(resultado);
        

    }

    @Test
    void testCalcularUnaSemanaAntes() {
        // Given
        LocalDate fecha = LocalDate.of(2026, 8, 15);

        // When
        LocalDate resultado = servicioEvento.calcularUnaSemanaAntes(fecha);

        // Then
        assertEquals(LocalDate.of(2026, 8, 8), resultado);
    }

    @Test
    void testVerificarDisponibilidadSinEventosDuracionMas2horas() {
        // Given
        // When
        boolean resultado = servicioEvento.verificarDisponibilidad(
                LocalDate.of(2026, 8, 15),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0));

        // Then
        assertTrue(resultado);
    }

    @Test
    void testVerificarDisponibilidadDuracionMenorDosHoras() {
        // Given

        // When
        boolean resultado = servicioEvento.verificarDisponibilidad(
                LocalDate.of(2026, 8, 15),
                LocalTime.of(10, 0),
                LocalTime.of(11, 30));

        // Then
        assertFalse(resultado);
    }

    @Test
    void testVerificarDisponibilidadEventoTraslapado() {
        // Given
        Evento evento = new Evento();
        evento.setFechaE(LocalDate.of(2026, 8, 15));
        evento.setHoraIn(LocalTime.of(10, 0));
        evento.setHoraFin(LocalTime.of(13, 0));
    
        when(repoEvento.findAll()).thenReturn(List.of(evento));
    
        // When
        boolean resultado = servicioEvento.verificarDisponibilidad(
                LocalDate.of(2026, 8, 15),
                LocalTime.of(12, 0),
                LocalTime.of(14, 0));
        
        // Then
        assertFalse(resultado);
    }

    @Test
    void testVerificarDisponibilidadMenosSeisHoras() {
        // Given
        Evento evento = new Evento();
        evento.setFechaE(LocalDate.of(2026, 8, 15));
        evento.setHoraIn(LocalTime.of(10, 0));
        evento.setHoraFin(LocalTime.of(12, 0));

        Mockito.when(repoEvento.findAll()).thenReturn(List.of(evento));

        // When
        boolean resultado = servicioEvento.verificarDisponibilidad(
                LocalDate.of(2026, 8, 15),
                LocalTime.of(15, 0),
                LocalTime.of(17, 0));

        // Then
        assertFalse(resultado);
    }

    @Test
    void testVerificarDisponibilidadMasSeisHoras() {
        // Given
        Evento evento = new Evento();
        evento.setFechaE(LocalDate.of(2026, 8, 15));
        evento.setHoraIn(LocalTime.of(8, 0));
        evento.setHoraFin(LocalTime.of(10, 0));

        Mockito.when(repoEvento.findAll()).thenReturn(List.of(evento));

        // When
        boolean resultado = servicioEvento.verificarDisponibilidad(
                LocalDate.of(2026, 8, 15),
                LocalTime.of(16, 0),
                LocalTime.of(18, 0));

        // Then
        assertTrue(resultado);
    }

    @Test
    void testVerificarDisponibilidadOtraFecha() {
        // Given
        Evento evento = new Evento();
        evento.setFechaE(LocalDate.of(2026, 8, 14));
        evento.setHoraIn(LocalTime.of(10, 0));
        evento.setHoraFin(LocalTime.of(12, 0));

        Mockito.when(repoEvento.findAll()).thenReturn(List.of(evento));

        // When
        boolean resultado = servicioEvento.verificarDisponibilidad(
                LocalDate.of(2026, 8, 15),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0));

        // Then
        assertTrue(resultado);
    }

    @Test
    void testVerificarDisponibilidadFechaAnterior() {
        // Given
        LocalDate fecha = LocalDate.now().minusDays(1);

        // When
        boolean resultado = servicioEvento.verificarDisponibilidad(
                fecha,
                LocalTime.of(10, 0),
                LocalTime.of(12, 0));

        // Then
        assertFalse(resultado);
    }

}