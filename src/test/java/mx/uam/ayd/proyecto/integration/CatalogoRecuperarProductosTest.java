package mx.uam.ayd.proyecto.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import mx.uam.ayd.proyecto.BaseIntegrationTest;
import mx.uam.ayd.proyecto.datos.repositorioProducto;
import mx.uam.ayd.proyecto.negocio.servicioProducto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;

public class CatalogoRecuperarProductosTest extends BaseIntegrationTest {
    
    @Autowired
    private servicioProducto servicioProducto;
    
    @Autowired
    private repositorioProducto repoProd;

    @Test
    @Transactional
    void TestRecuperarProductosTodo(){

        //caso 1: recuperar todos los productos que estan guardados en el repositorio
        //given
        String mezcales = "Mezcal";

        //creo producto de prueba
        Producto prod = new Producto();
        Producto prod2 = new Producto();
        prod.setTipoProd(mezcales);
        prod2.setTipoProd(mezcales);
        
        //guardamos el producto en el repositorio
        repoProd.save(prod);
        repoProd.save(prod2);
        
        //when
        List<Producto> prodDisponibles = servicioProducto.obtenerProductosDisponibles();
        
        //then
        assertEquals(2, prodDisponibles.size());
    }
    
    @Test
    @Transactional
    void TestRecupoerarProductosCategoria(){
        //given
        String categoria = "Mezcal";
        
        Producto prod = new Producto();
        Producto prod2 = new Producto();
        prod.setTipoProd(categoria);
        prod2.setTipoProd(categoria);
        
        repoProd.save(prod);
        repoProd.save(prod2);
        
        //when
        List<Producto> filtro = servicioProducto.obtenerProductoPorCriterio(categoria);

        //then
        assertEquals(2, filtro.size());
    }
}
