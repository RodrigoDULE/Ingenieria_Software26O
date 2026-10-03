package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.repositorioProducto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.AsistenteBot;

@ExtendWith(MockitoExtension.class)
public class ServicioBotTest {
    
    /*
    @Mock
    private repositorioProducto repoProd;
    
    @Mock
    AsistenteBot bot;
    
    @InjectMocks
    private Serviciobot serbot;
    
    @Test
    void TestProcesaMensajeDameDatos() {
        // given
        String mensaje = "¿Que mezcales hay disponibles en esta tienda?";
        
        // ¡Aquí le decimos al mock cómo comportarse!
        // Le indicamos que cuando llamen a palabrasClave con cualquier texto, devuelva "mezcal"
        when(bot.palabrasClave(anyString())).thenReturn("mezcal");
        
        // when
        String res = serbot.procesaMensajeDameDatos(mensaje);
        
        // then
        assertNotNull(res);
    }
    */
}
