package mx.uam.ayd.proyecto.negocio.EntidadNegocio;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class carritoCompraTest {
    
    private Producto productoPrueba;
    private carritoCompra carritoPrueba;

    @BeforeEach
    void Inicializar(){
        productoPrueba = new Producto();
        carritoPrueba = new carritoCompra();

        productoPrueba.setnombre("Mezcal v2.0");
        productoPrueba.setPrecio(18.5f);
        productoPrueba.setcantidadStock(25);
    }

    @Test
    @DisplayName("Agrega Producto al carrito")
    void TestsetProducto(){
        //when
        boolean test = carritoPrueba.setProducto(productoPrueba);
        
        //then 
        assertTrue(test, "Deberia dejar agregar el producto");
    }

    @Test
    @DisplayName("No agrega producto duplicado")
    void TestProdDuplicado(){
        //given 
        carritoPrueba.setProducto(productoPrueba);

        //when
        boolean test = carritoPrueba.setProducto(productoPrueba);

        //then
        assertFalse(test, "No deberia agregar el producto");
    }

    @Test
    @DisplayName("Deberia remover el Producto del carrito")
    void TestRemoverProducto(){
        //given 
        carritoPrueba.setProducto(productoPrueba);
        //when
        boolean test = carritoPrueba.removerProducto(productoPrueba, 15);
        //then
        assertTrue(test, "Deberia remover el producto");
    }

    @Test
    @DisplayName("No deberia remover un producto que no existe dentro del carrito")
    void TestremoverProductoInexistente(){
        
        //when
        boolean test = carritoPrueba.removerProducto(productoPrueba, 48);

        //then
        assertFalse(test, "No se deberia remover nada dentro del carrito");
    }

    @Test
    @DisplayName("Deberia regresar un true, ya que la cantidad comprada es mayor a 320 pesos")
    void envioGratisTest(){
        //given
        carritoPrueba.setTotalCalculado(350);
        carritoPrueba.validarEnvioGratis();

        //when
        boolean gratis = carritoPrueba.getenvioGratis();

        //then
        assertTrue(gratis);
    }
}
