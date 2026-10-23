package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Resena;
import mx.uam.ayd.proyecto.datos.RepositorioResena;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServicioResenaTest {

    @Mock
    private RepositorioResena repositorioResena;

    @InjectMocks
    private ServicioResena servicioResena;

    @Test
    public void testCrearResenaExitososa() {
        // Se crea  un producto de prueba vacío pero válido
        Producto productoPrueba = new Producto();
        
        int calificacion = 5;
        String titulo = "Excelente";
        String comentario = "Muy buen producto";

        // Aqui se simula el repositorio guarda con éxito
        when(repositorioResena.save(any(Resena.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Ejecutam el servicio
        Resena resultado = servicioResena.crearResena(calificacion, titulo, comentario, productoPrueba);

        // Verificam resultados
        assertNotNull(resultado);
        assertEquals(5, resultado.getCalificacion());
        assertEquals("Excelente", resultado.getTitulo());
        assertEquals(productoPrueba, resultado.getProducto());

        verify(repositorioResena, times(1)).save(any(Resena.class));
    }

    @Test
    public void testCrearResenaCalificacionInvalida() {
        Producto productoPrueba = new Producto();

        // Probamos con una calificación fuera de rango (ej. 6)
        Exception excepcion = assertThrows(IllegalArgumentException.class, () -> {
            servicioResena.crearResena(6, "Malo", "No sirve", productoPrueba);
        });

        assertEquals("La calificación debe ser de 1 a 5 estrellas.", excepcion.getMessage());
        verify(repositorioResena, never()).save(any(Resena.class));
    }
}