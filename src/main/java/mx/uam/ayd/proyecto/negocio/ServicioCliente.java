package mx.uam.ayd.proyecto.negocio;

import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.repositorioCliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;

@Service
public class ServicioCliente {
    
    private final repositorioCliente repoCliente;

    //constructor
    public ServicioCliente(repositorioCliente repoCliente){
        this.repoCliente = repoCliente;
    }


    public Cliente dameCliente(String Nombre, String Contraseña){
        if(repoCliente.findByNombreAndContraseña(Nombre, Contraseña) == null){
            return null;
        }
        return repoCliente.findByNombreAndContraseña(Nombre, Contraseña);


    }


}
