package mx.uam.ayd.proyecto.negocio.EntidadNegocio;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class EventoTest {
    private Evento eventoPrueba;
    private List<Empleado> empleadosPrueba;

    @BeforeEach
    void Inicializar() {
        eventoPrueba = new Evento("Evento de prueba", "Lugar de prueba", 10, 100,
                LocalDate.of(2024, 6, 15), LocalTime.of(10, 0), LocalTime.of(12, 0),
                LocalDate.of(2024, 6, 14), "Tipo de prueba", "Acuerdo económico de prueba",
                "Notas adicionales de prueba");

        empleadosPrueba = new ArrayList<>();
        empleadosPrueba.add(new Empleado("Empleado de prueba", "1234567890"));
        empleadosPrueba.add(new Empleado("Empleado de prueba 2", "0987654321"));
        empleadosPrueba.add(new Empleado("Empleado de prueba 3", "1122334455"));

    }   

    @Test
    @DisplayName("Deberia obtener empleados al evento")
    void testGetEmpleados() {
        // Given
        Evento evento = new Evento();

        List<Empleado> empleados = new ArrayList<>();
        empleados.add(new Empleado());
        empleados.add(new Empleado());

        evento.getEmpleados().addAll(empleados);

        // When
        List<Empleado> resultado = evento.getEmpleados();

        // Then
        assertEquals(2, resultado.size());
        assertEquals(empleados, resultado);
    }

}


