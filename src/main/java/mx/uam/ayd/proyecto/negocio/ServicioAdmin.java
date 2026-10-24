package mx.uam.ayd.proyecto.negocio;

import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.repositorioAdmin;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Administrador;

@Service
public class ServicioAdmin {

    private final repositorioAdmin repoAdmin;


    public ServicioAdmin(repositorioAdmin repoAdmin){
        this.repoAdmin = repoAdmin;
    }

    public Administrador dameAdmin(String Nombre, String Contraseña){
        if(repoAdmin.findByNombreAndContraseña(Nombre, Contraseña) == null){
            return null;
        }
        return repoAdmin.findByNombreAndContraseña(Nombre, Contraseña);
    }
}
