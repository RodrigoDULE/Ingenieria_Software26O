package mx.uam.ayd.proyecto.negocio.EntidadNegocio;
import java.util.List;

import jakarta.persistence.Entity;

import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity 
public class Administrador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idAdmin;


    private String nombre;
    private String contraseña;

    public Administrador(String nombre, String contraseña){
        this.nombre = nombre;
        this.contraseña = contraseña;
    }


    //constructor 

    public Administrador(){}

    public long getidAdmin(){return idAdmin;}
    public void setidAdmin(long idAdmin){this.idAdmin = idAdmin;}
    public String getNombre(){return nombre;}
    public void setNombre(String nombre){this.nombre = nombre;}
    public String getContraseña(){return contraseña;}
    public void setContraseña(String contraseña){this.contraseña = contraseña;} 


}
