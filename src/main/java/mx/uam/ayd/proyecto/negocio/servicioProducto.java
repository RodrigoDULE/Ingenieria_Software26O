package mx.uam.ayd.proyecto.negocio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.repositorioProducto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;

@Service
public class servicioProducto {
    private final repositorioProducto repoProd;

    public servicioProducto(repositorioProducto repoProd){
        this.repoProd = repoProd;
    }

    public Producto agregarProducto(String nombre, float precio, String Descripcion, int CantidadStock, int contenidoMCL, float GradoAlcohol, String tipoProd, String imagen,  String tipoAgave, String region){

        //Validaciones campos de texto
        if (nombre == null || nombre.trim().isEmpty())
        {throw new IllegalArgumentException("El nombre no puede ser nulo o estar vacío");}

        if (tipoProd == null || tipoProd.trim().isEmpty())
        {throw new IllegalArgumentException("El tipo de producto no puede ser nulo o estar vacío");}

        if (Descripcion == null || Descripcion.trim().isEmpty())
        {throw new IllegalArgumentException("El tipo de producto no puede ser nulo o estar vacío");}
        
        // Validaciones numéricas

        if(precio <= 0){
            throw new IllegalArgumentException("El precio unitario debe ser mayor que cero");
        }

        if(CantidadStock <= 0){
            throw new IllegalArgumentException("El stock inicial debe ser mayor que cero");
        }

        Producto nuevoProducto = new Producto(nombre, precio, Descripcion,CantidadStock,contenidoMCL,GradoAlcohol,tipoProd,imagen, tipoAgave,region);


        return repoProd.save(nuevoProducto); 
    }

    public List<Producto> obtenerProductosDisponibles(){
        List<Producto> todoProducto = new ArrayList<Producto>();

        //Creamos un for each para meter cada producto dentro del arreglo
        for(Producto prod : repoProd.findAll()){ //findAll es iterador
            todoProducto.add(prod);
        }

        return todoProducto;
    }

    public List<Producto> obtenerProductoPorCriterio(String criterio){
        List<Producto> ProdCriterio = new ArrayList<Producto>();
        
        for(Producto a: repoProd.findBytipoProd(criterio)){
            ProdCriterio.add(a);
        }
        
        return ProdCriterio;
    }

    public List<Producto> obtenerPorBusquedaPersonalizada(String criterio){
        if(repoProd.findByNombreContainingIgnoreCase(criterio).isEmpty()){
            return Collections.emptyList(); // se retorna una lista vacia en caso de que la lista este, vacia
        }

        return repoProd.findByNombreContainingIgnoreCase(criterio);
    }
}
