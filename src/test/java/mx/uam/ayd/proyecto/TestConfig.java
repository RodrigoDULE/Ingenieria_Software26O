package mx.uam.ayd.proyecto;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import mx.uam.ayd.proyecto.presentacion.HU01AgregarInsumoCarrito.DetallesProductoAgregarCarrito.vistaDetallesProductoAgregarCarrito;
import mx.uam.ayd.proyecto.presentacion.HU01AgregarInsumoCarrito.catalogoMezicuil.vistaCatalogoMezicuil;
import mx.uam.ayd.proyecto.presentacion.HU02CarritoPrincipal.vistaCarritoPrincipal;
import mx.uam.ayd.proyecto.presentacion.Principal.vistaPrincipal;

@TestConfiguration
@Profile("test")
@EnableTransactionManagement
public class TestConfig {

    @Bean
    @Primary
    public vistaPrincipal vistaPrincipal() {
        return new vistaPrincipal();
    }

    @Bean
    @Primary
    public vistaCarritoPrincipal vistaCarritoPrincipal() {
        return new vistaCarritoPrincipal();
    }

    @Bean
    @Primary
    public vistaDetallesProductoAgregarCarrito vistaDetallesProductoAgregarCarrito() {
        return new vistaDetallesProductoAgregarCarrito();
    }

    @Bean
    @Primary
    public vistaCatalogoMezicuil vistaCatalogoMezicuil() {
        return new vistaCatalogoMezicuil();
    }
} 