package mx.uam.ayd.proyecto.negocio;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.negocio.EntidadNegocio.FormularioMarketing;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.FormularioMarketing.TipoContenido;


// Procesa los datos del formulario de marketing y devuelve las variaciones
// solicitadas (simuladas mediante textos o referencias visuales predefinidas).
@Service
public class ServicioGeneracionContenido {

    // Almacena la información que será desplegada en las tarjetas de la vista de resultados.
    public static class VariacionContenido {

        private String nombre;
        private String descripcion;

        public VariacionContenido(String nombre, String descripcion) {
            this.nombre = nombre;
            this.descripcion = descripcion;
        }

        public String getNombre() {
            return nombre;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    // Recibe la entidad del formulario guardado y delega la creación de propuestas.
    public List<VariacionContenido> generarVariaciones(FormularioMarketing formularioGuardado) {
        return simularGeneracion(formularioGuardado);
    }

    // Construye la lista de variaciones aplicando reglas de negocio basadas 
    // en el tipo de contenido y la cantidad solicitada por el usuario.
    private List<VariacionContenido> simularGeneracion(FormularioMarketing formulario) {
        List<VariacionContenido> variaciones = new ArrayList<>();

        int cantidad = formulario.getCantidadVariaciones() != null ? formulario.getCantidadVariaciones() : 1;
        TipoContenido tipoContenido = formulario.getTipoContenido();

        // Arreglo de frases de ejemplo para simular copys/textos de marketing.
        String[] textosMarketing = {
            "¡Elixir ancestral para el alma! Descubre el sabor auténtico de nuestro mezcal artesanal, hecho 100% de agave de origen.",
            "Para todo mal, mezcal; para todo bien, también. Disfruta una tradición viva llena de carácter y aroma ahumado.",
            "Sabor que honra la tierra. Cada gota cuenta una historia de maestría, tradición y pasion palenquera. ¡Pide el tuyo!"
        };

        for (int i = 0; i < cantidad; i++) {
            char letra = (char) ('A' + i);
            String nombre;
            String descripcion;

            if (tipoContenido == TipoContenido.SOLO_TEXTO) {
                // Para SOLO_TEXTO asignamos un copy persuasivo al campo 'nombre' 

                nombre = textosMarketing[i % textosMarketing.length];
                descripcion = "            VARIACIÓN " + letra ;
            } else {
                // Para IMAGEN_ESTATICA u otros tipos visuales, el nombre asume 
                // un formato genérico que acompaña a la imagen renderizada
                nombre = "Variación " + letra;
                descripcion = "            VARIACIÓN " + letra;
            }

            variaciones.add(new VariacionContenido(nombre, descripcion));
        }

        return variaciones;
    }
}