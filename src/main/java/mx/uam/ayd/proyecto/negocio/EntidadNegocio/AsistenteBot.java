package mx.uam.ayd.proyecto.negocio.EntidadNegocio;

import com.fasterxml.jackson.databind.ObjectMapper;

/*google me da esas librerias, las cuales no son soportadas por la dependencia estable del sdk, tambien me las da porque tengo activada la opcion de groundint with google search
Hasta ahora no hay una version estable que soporte
import com.google.genai.interactions.models.interactions.CreateModelInteractionParams;
import com.google.genai.interactions.models.interactions.Content;
import com.google.genai.interactions.models.interactions.Input;
import com.google.genai.interactions.models.interactions.Interaction;
import com.google.genai.interactions.models.interactions.Step;
import com.google.genai.interactions.models.interactions.TextContent;
import com.google.genai.interactions.models.interactions.ImageContent;
import com.google.genai.interactions.models.interactions.AudioContent;
import com.google.genai.interactions.models.interactions.Tool;
import com.google.genai.interactions.models.interactions.Function;
import com.google.genai.interactions.models.interactions.GenerationConfig;
import com.google.genai.interactions.models.interactions.GoogleSearch;
import com.google.genai.interactions.core.JsonValue;
import com.google.genai.Client;
import com.google.common.collect.ImmutableList;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import com.google.genai.JsonSerializable;

public class AsistenteBot  {
    public static void main(String[] args) {
        String apiKey = System.getenv("GEMINI_API_KEY");
        Client client = Client.builder().apiKey(apiKey).build();
        
        CreateModelInteractionParams.Builder paramsBuilder =
        CreateModelInteractionParams.builder()
        .model("models/gemini-3-flash-preview");
        
        paramsBuilder = paramsBuilder.input("");
        paramsBuilder = paramsBuilder.systemInstruction("\".");
        paramsBuilder = paramsBuilder.generationConfig(GenerationConfig.builder()
        .temperature(0.2f)
        .maxOutputTokens(65536)
        .topP(0.95f)
        .build());
        CreateModelInteractionParams params = paramsBuilder.build();
        
        Interaction interaction = client.interactions.create(params);
        
        for (Step step : interaction.steps()) {
            if (step.isModelOutput()) {
                step.asModelOutput().content().ifPresent(contents -> {
                    for (Content output : contents) {
                        output.text().ifPresent(text -> System.out.println(text.text()));
                    }
                });
            }
        }
    }
}
*/

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AsistenteBot {

    AsistenteBot() {
    }

    private String msg;
    // 1. Inicializar el cliente (Automáticamente toma la variable de entorno
    // GEMINI_API_KEY)
    // 1. Inicializar el cliente pasando la API Key explícitamente
    String miApiKey = "AQUI_VA_MI_LLAVE";
    Client client = Client.builder().apiKey(miApiKey).build();

    // 2. Definir las instrucciones del sistema (System Instruction)
    String instrucciones = "Eres el asistente virtual exclusivo de Mezcaleria Mezicuil. Tu único propósito es ayudar a los clientes con información sobre productos, disponibilidad, precios, compras y políticas de envío de la tienda.\\\"\\n\\nReglas:\\n\\nResponde siempre de forma amable, concisa y profesional.\\n\\nSi el usuario te saluda, salúdalo e indícale en qué puedes ayudarle respecto a la tienda.\\n\\nSi el usuario te hace preguntas ajenas a la tienda (deportes, cultura general, recetas, tareas, etc.), responde únicamente: 'Lo siento, solo puedo ayudarte con consultas relacionadas con los productos y servicios de nuestra tienda en línea.'\\n\\nNunca salgas de tu rol ni respondas preguntas que no correspondan a e-commerce";
    Content systemInstruction = Content.builder()
            .parts(Arrays.asList(Part.builder().text(instrucciones).build()))
            .build();

    // 3. Configurar los parámetros de generación
    GenerateContentConfig config = GenerateContentConfig.builder()
            .systemInstruction(systemInstruction)
            .temperature(0.2f)
            .maxOutputTokens(65536)
            .topP(0.95f)
            .build();

    public String palabrasClave(String MensajeUsuario) {
        this.msg = MensajeUsuario;
        // 4. Definir el modelo y el mensaje del usuario
        String nombreModelo = "gemini-3-flash-preview";
        String mensajeUsuario = "Obten unicamente una palabra clave, la que creas que tiene mas relacion en este contexto (Mezcal, Complementos, Todo): "
                + MensajeUsuario;
        // 5. Llamar a la API e imprimir la respuesta
        try {
            GenerateContentResponse response = client.models.generateContent(
                    nombreModelo,
                    mensajeUsuario,
                    config);

            // Reemplaza toda la lógica compleja de iteración por esta simple línea:
            //System.out.println(response.text());
            return response.text();

        } catch (Exception e) {
            System.err.println("Error al comunicarse con Gemini: " + e.getMessage());
        }

        return null;

    }

    public String generaMensajeRespuesta(List<Producto> prod) {
        //pertenece a la biblioteca principal jackson que sirve para convertir objetos en dormato JSON y viceversa
        ObjectMapper mapper = new ObjectMapper();
        // 5. Llamar a la API e imprimir la respuesta
        try {
            //Transforma la lista de java a un String en formato JSON
            String producto = mapper.writeValueAsString(prod);
            // 4. Definir el modelo y el mensaje del usuario
            String nombreModelo = "gemini-3-flash-preview";
            String mensajeUsuario = "Aquí tienes nuestro catálogo actual en formato JSON:\\n"
            + producto + "Con respeto a ello, genera una respuesta respondiendo a la pregunta del cliente " + msg;

            GenerateContentResponse response = client.models.generateContent(
                    nombreModelo,
                    mensajeUsuario,
                    config);

            // Reemplaza toda la lógica compleja de iteración por esta simple línea:
            //System.out.println(response.text());
            return response.text();

        } catch (Exception e) {
            System.err.println("Error al comunicarse con Gemini: " + e.getMessage());
        }

        return null;

    }

}
