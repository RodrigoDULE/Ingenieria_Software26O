package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.repositorioProducto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;

@ExtendWith(MockitoExtension.class)
public class servicioProductoTest {

    @Mock
    private repositorioProducto repoProd;

    @InjectMocks
    private servicioProducto servProd;

    //Criterio de aceptacion: Regresa todos los productos que existen dentro del repositorio
    @Test
    void TestObtenerProductosDisponibles(){
        //given 
       Producto clienteFalso = new Producto();
       Producto clienteFalso2 = new Producto();

       List<Producto> prod = new ArrayList<>();
       prod.add(clienteFalso);
       prod.add(clienteFalso2);

       when(repoProd.findAll()).thenReturn(prod);
        //when
       List<Producto> productos = servProd.obtenerProductosDisponibles();

       //then
       assertEquals(2, productos.size());
    }
    
    //Criterio de Aceptacion 1: Seleccion de algun filtro que regresa una lusta de productos
    @Test
    void TestobtenerProductosporCriterio(){
        //given, le tengo que agregar productos Mezcla
        String crierio = "Mezcal";
        List<Producto> prod = new ArrayList<>();
        Producto prd = new Producto();
        Producto prd2 = new Producto();

        prod.add(prd2);
        prod.add(prd);
        when(repoProd.findBytipoProd(crierio)).thenReturn(prod);
        
        //when
        List<Producto> crit = servProd.obtenerProductoPorCriterio(crierio);
        
        //then
        assertEquals(2, crit.size());
    }
    
    //Criterio de Acpetacion 2: Busqueda personalizada que regresa una lista de productos o solo una lista vacia
    @Test
    void TestObtenerProBusquedaPersonalizada(){
        //caso 1: retorna una lista vacia
        //given
        String Crit = "alguno";
        List<Producto> encontrado = new ArrayList<>();
        when(repoProd.findByNombreContainingIgnoreCase(Crit)).thenReturn(encontrado);
        
        //when
        List<Producto> esta = servProd.obtenerPorBusquedaPersonalizada(Crit);
        
        //then
        assert(esta.isEmpty());
        
        //caso 2: Retorna una lista con productos
        //given
        Producto prd = new Producto();
        Producto prd2 = new Producto();
        encontrado.add(prd);
        encontrado.add(prd2);
        when(repoProd.findByNombreContainingIgnoreCase(Crit)).thenReturn(encontrado);
        //when
        List<Producto> estaLLena = servProd.obtenerPorBusquedaPersonalizada(Crit);
        //then
        assertEquals(2, estaLLena.size());

    }
}
