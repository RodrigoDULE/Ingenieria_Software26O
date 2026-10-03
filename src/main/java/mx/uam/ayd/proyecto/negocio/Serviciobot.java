package mx.uam.ayd.proyecto.negocio;

import java.util.List;

import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.repositorioProducto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.AsistenteBot;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;

@Service
public class Serviciobot {
    
    repositorioProducto repoProd;
    //creo la instancia con el bot
    AsistenteBot bot;

    Serviciobot(repositorioProducto repoProd, AsistenteBot bot){
        this.repoProd = repoProd;
        this.bot = bot;
    }

    public String procesaMensajeDameDatos(String msg){
        String pk= bot.palabrasClave(msg);
        System.out.println("La palabra clave generada por la IA: " + pk);

        List<Producto> prod = repoProd.findByNombreContainingIgnoreCase(pk);

        String finalReponse = bot.generaMensajeRespuesta(prod);
        //System.out.println("Res generada: " + finalReponse);

        return finalReponse;
    }

}
