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
