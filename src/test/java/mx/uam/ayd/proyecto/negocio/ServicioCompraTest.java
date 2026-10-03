package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.repositorioCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Compra;

@ExtendWith(MockitoExtension.class)
public class ServicioCompraTest {

    @Mock
    private repositorioCompra repoCompra;

    @InjectMocks
    private ServicioCompra servicioCompra;

    @Test
    void testGuardarCompra() {
        // given
        float monto = 150.5f;
        LocalDate fecha = LocalDate.of(2026, 7, 27);

        Compra compraGuardada = new Compra();
        compraGuardada.setIdCompra(42L);
        compraGuardada.setMonto(monto);
        compraGuardada.setFecha(fecha);

        when(repoCompra.save(any(Compra.class)))
                .thenReturn(compraGuardada);

        // when
        Compra result = servicioCompra.guardarCompra(monto, fecha);

        // then
        assertNotNull(result);
        assertEquals(42L, result.getIdCompra());
        assertEquals(monto, result.getMonto());
        assertEquals(fecha, result.getFecha());

    }

    @Test
    void testDameCompra() {
        // given
        long id = 7L;
        Compra c = new Compra();
        c.setIdCompra(id);
        c.setMonto(200f);

        when(repoCompra.findByIdCompra(id)).thenReturn(c);

        // when
        Compra result = servicioCompra.dameCompra(id);

        // then
        assertNotNull(result);
        assertEquals(id, result.getIdCompra());
    }

    @Test
    void testListaCompras() {
        // given
        Compra c1 = new Compra();
        c1.setIdCompra(1L);
        Compra c2 = new Compra();
        c2.setIdCompra(2L);

        when(repoCompra.findAll()).thenReturn(List.of(c1, c2));

        // when
        Iterable<Compra> resultado = servicioCompra.listaCompras();

        // then
        int count = 0;
        for (Compra x : resultado) {
            count++;
        }
        assertEquals(2, count);
    }
}
