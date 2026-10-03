package mx.uam.ayd.proyecto.negocio;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.RepositorioEmpleado;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Empleado;

@ExtendWith(MockitoExtension.class)
public class servicioEmpleadoTest {
    @Mock
    private RepositorioEmpleado repoEmpleado;

    @InjectMocks
    private servicioEmpleado servicioEmpleado;

    @Test
    void testObtenerNombreEmpleados() {
        // Caso 1: retorna una lista vacía si no hay empleados

        // given
        List<String> listaNombres = new ArrayList<>();
        when(repoEmpleado.findAllNombres()).thenReturn(listaNombres);

        // when
        List<String> nombres = servicioEmpleado.obtenerNombreEmpleados();

        // then
        assertEquals(0, nombres.size());
    }

    @Test
    void testObtenerNombreEmpleadosConDatos() {
        // Given
        List<String> listaNombres = new ArrayList<>();
        listaNombres.add("Juan");
        listaNombres.add("Maria");
        listaNombres.add("Pedro");

        when(repoEmpleado.findAllNombres()).thenReturn(listaNombres);

        // When
        List<String> nombres = servicioEmpleado.obtenerNombreEmpleados();

        // Then
        assertEquals(3, nombres.size());
        assertEquals("Juan", nombres.get(0));
        assertEquals("Maria", nombres.get(1));
        assertEquals("Pedro", nombres.get(2));
    }

    @Test
    void testobtenerEmplpeadosPorNombreNoEncontrados() {
        // Caso 1: retorna una lista vacía si no hay empleados con los nombres dados

        // given
        List<String> nombresEmpleado = new ArrayList<>();
        nombresEmpleado.add("Juan");
        nombresEmpleado.add("Maria");

        List<Empleado> listaEmpleados = new ArrayList<>();
        when(repoEmpleado.findByNombreEmpleadoIn(nombresEmpleado)).thenReturn(listaEmpleados);

        // when
        List<Empleado> empleados = servicioEmpleado.obtenerEmpleadosPorNombre(nombresEmpleado);

        // then
        assertEquals(0, empleados.size());
    }

@Test
    void testobtenerEmplpeadosPorNombreEncontrados() {
        // Caso 2: retorna una lista vacía si no hay empleados con los nombres dados

        // given
        List<String> nombresEmpleado = new ArrayList<>();
        nombresEmpleado.add("Juan");
        nombresEmpleado.add("Maria");

        List<Empleado> listaEmpleados = new ArrayList<>();
        Empleado empleado1 = new Empleado();
        empleado1.setNombreEmpleado("Juan");
        Empleado empleado2 = new Empleado();
        empleado2.setNombreEmpleado("Maria");
        listaEmpleados.add(empleado1);
        listaEmpleados.add(empleado2);

        when(repoEmpleado.findByNombreEmpleadoIn(nombresEmpleado)).thenReturn(listaEmpleados);

        // when
        List<Empleado> empleados = servicioEmpleado.obtenerEmpleadosPorNombre(nombresEmpleado);

        // then
        assertEquals(2, empleados.size());
    }
}