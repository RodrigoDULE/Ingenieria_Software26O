package mx.uam.ayd.proyecto.negocio;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.datos.RepositorioInsumo;
import mx.uam.ayd.proyecto.datos.RepositorioOrdenDeCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Insumo;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.DetalleOrden;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra.EstadoOrden;

@Component
public class DatosPruebaInitializer implements CommandLineRunner {

    private final RepositorioInsumo repositorioInsumo;
    private final RepositorioOrdenDeCompra repositorioOrden;

    public DatosPruebaInitializer(RepositorioInsumo repositorioInsumo, RepositorioOrdenDeCompra repositorioOrden) {
        this.repositorioInsumo = repositorioInsumo;
        this.repositorioOrden = repositorioOrden;
    }

    @Override
    public void run(String... args) throws Exception {
        
        if (repositorioOrden.count() == 0) {
            System.out.println(" Inicializando datos de prueba para las Órdenes de Compra");

            
            Insumo mezcalEspadin = new Insumo("Mezcal Espadin Joven a Granel", 180.00, "Destilería Los Abuelos S.A. de C.V.", "Litros", "MEZ-ESP-01");
            Insumo botellasVidrio = new Insumo("Botellas de Vidrio 750ml Tipo B", 15.50, "Destilería Los Abuelos S.A.", "Unidades", "BVS-750-01");
            Insumo etiquetasFinas = new Insumo("Etiquetas Finas de México", 3.15, "Etiquetas Finas S.A.", "Unidades", "ETQ-FIN-001");

            repositorioInsumo.save(mezcalEspadin);
            repositorioInsumo.save(botellasVidrio);
            repositorioInsumo.save(etiquetasFinas);

            // orden 1
            OrdenDeCompra ordenAbuelos = new OrdenDeCompra();
            ordenAbuelos.setEstadoOrden(EstadoOrden.REVISION_PENDIENTE);
            ordenAbuelos.setFactura("PO-2026-089"); 
            ordenAbuelos.setFechaCreacion(LocalDate.now());
            
            DetalleOrden detalle1 = new DetalleOrden();
            detalle1.setCantidad(500);
            detalle1.setInsumo(mezcalEspadin);
            detalle1.setSubTotalLote(500 * mezcalEspadin.getPrecio());
            
            DetalleOrden detalle2 = new DetalleOrden();
            detalle2.setCantidad(700); 
            detalle2.setInsumo(botellasVidrio);
            detalle2.setSubTotalLote(700 * botellasVidrio.getPrecio()); 

            ordenAbuelos.agregarDetalle(detalle1);
            ordenAbuelos.agregarDetalle(detalle2);

            double totalOrden1 = detalle1.getSubTotalLote() + detalle2.getSubTotalLote(); 
            ordenAbuelos.setTotalOrdenCompra(totalOrden1); 
            repositorioOrden.save(ordenAbuelos);

            // orden 2
            OrdenDeCompra ordenEtiquetas = new OrdenDeCompra();
            ordenEtiquetas.setEstadoOrden(EstadoOrden.REVISION_PENDIENTE);
            ordenEtiquetas.setFactura("PO-2026-044");
            ordenEtiquetas.setFechaCreacion(LocalDate.now());
            
            DetalleOrden detalle3 = new DetalleOrden();
            detalle3.setCantidad(100); 
            detalle3.setInsumo(etiquetasFinas);
            detalle3.setSubTotalLote(100 * etiquetasFinas.getPrecio()); 

            ordenEtiquetas.agregarDetalle(detalle3);
            ordenEtiquetas.setTotalOrdenCompra(detalle3.getSubTotalLote()); 
            repositorioOrden.save(ordenEtiquetas);

            // orden 3 renviada ya
            OrdenDeCompra ordenRecepcion = new OrdenDeCompra();
            ordenRecepcion.setEstadoOrden(EstadoOrden.ENVIADA); // esta ya se puede recibir
            ordenRecepcion.setFactura("PO-2023-102");
            ordenRecepcion.setFechaCreacion(LocalDate.now().minusDays(5)); // se creó hace 5 días
            ordenRecepcion.setFechaEnvio(LocalDate.now().minusDays(2));    // enviada hace hace 2 días
            
            
            DetalleOrden detalle4 = new DetalleOrden();
            detalle4.setCantidad(3); // escanearemos solo 3 botellas
            detalle4.setInsumo(botellasVidrio);
            detalle4.setSubTotalLote(3 * botellasVidrio.getPrecio()); 

            DetalleOrden detalle5 = new DetalleOrden();
            detalle5.setCantidad(2); // Y solo 2 etiquetas
            detalle5.setInsumo(etiquetasFinas);
            detalle5.setSubTotalLote(2 * etiquetasFinas.getPrecio()); 

            ordenRecepcion.agregarDetalle(detalle4);
            ordenRecepcion.agregarDetalle(detalle5);
            ordenRecepcion.setTotalOrdenCompra(detalle4.getSubTotalLote() + detalle5.getSubTotalLote()); 
            
            repositorioOrden.save(ordenRecepcion);

            System.out.println("datos de prueba creados con éxito. Se generaron 3 órdenes.");
        } else {
            System.out.println("ya existen órdenes en la base de datos, nada nuevo que hacer.");
        }
    }
}