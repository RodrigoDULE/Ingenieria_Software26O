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

    // Criterio de Aceptación HU-01 (Escenario 1): Registro exitoso de un producto
    @Test
    void TestAgregarProductoExitoso(){
        //given (Dado este escenario de datos válidos)
        String nombre = "Mezcal Espadín";
        float precio = 500.50f;
        String Descripcion = "Mezcal ahumado, barricas de cedro";
        int CantidadStock = 20;
        int contenidoMCL =750;
        float GradoAlcohol = 40.0f;
        String tipoProd = "Mezcales";
        String imagen = "ruta/imagen.jpg";
        int volumen = 750;
        String tipoAgave = "Espadín";
        String region = "Oaxaca";

        //Simulación de producto 
        Producto productoGuardado = new Producto(nombre, precio, Descripcion, CantidadStock, contenidoMCL, GradoAlcohol, tipoProd, imagen, volumen, tipoAgave, region);

        //Configuración de mock
        when(repoProd.save(org.mockito.ArgumentMatchers.any(Producto.class))).thenReturn(productoGuardado);

        //Cuando el repositorio llame a "save", retorna productoGuardado
        Producto resultado = servProd.agregarProducto(nombre, precio, Descripcion, CantidadStock, contenidoMCL, GradoAlcohol, tipoProd, imagen, volumen, tipoAgave, region);

        //Entonces verificamos que el comportamiento sea el esperado
        assertNotNull(resultado, "El producto devuelto no debería ser nulo");
        assertEquals("Mezcal Espadín",resultado.getnombre(),"El nombre del producto devuelto debe coincidir");

        //Comprobamos que el servicio realmente mandó a llamar al método "save" del repositorio exactamente 1 vez
        org.mockito.Mockito.verify(repoProd).save(org.mockito.ArgumentMatchers.any(Producto.class));
    }

    // Criterio de aceptación HU-01 (Escenarios 2, 3 y 4): Fallos por datos inválidos
    @Test
    void TestAgregarProductoFallos(){
    // Escenario 2: Validar fallo por campo de texto vacío (Nombre)
        assertThrows(IllegalArgumentException.class, () -> {
            // Intentamos guardar un producto con el nombre vacío ("")
            servProd.agregarProducto("", 500.50f, "Descripción válida", 20, 750, 40.0f, "Mezcales", "ruta.jpg", 750, "Espadín", "Oaxaca");
        });

    // Escenario 3: Validar fallo por precio inválido (<= 0)
        assertThrows(IllegalArgumentException.class, () -> {
            // Intentamos guardar un producto con precio de 0
            servProd.agregarProducto("Mezcal Válido", 0.0f, "Descripción válida", 20, 750, 40.0f, "Mezcales", "ruta.jpg", 750, "Espadín", "Oaxaca");
        });

        // Escenario 4: Validar fallo por stock inválido 
        assertThrows(IllegalArgumentException.class, () -> {
            // Intentamos guardar un producto con stock de 0
            servProd.agregarProducto("Mezcal Válido", 500.50f, "Descripción válida", 0, 750, 40.0f, "Mezcales", "ruta.jpg", 750, "Espadín", "Oaxaca");
        });
        
        // Verificación de que el repositorio NUNCA fue llamado para guardar
        org.mockito.Mockito.verify(repoProd, org.mockito.Mockito.never()).save(org.mockito.ArgumentMatchers.any(Producto.class));
    }

}
