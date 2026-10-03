
package mx.uam.ayd.proyecto.negocio.EntidadNegocio;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;




@Entity
public class OrdenDeCompra {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    public enum EstadoOrden
    {
        REVISION_PENDIENTE,
        AUTORIZADA,
        ENVIADA,
        RECIBIDA
    }

    @Enumerated(EnumType.STRING)
    private EstadoOrden estado;

    private double total;
    private LocalDate fechaEnvio;
    private LocalDate fechaRecepcion;
    private LocalDate fechaCreacion;
    private String factura; 

    // Relacion uno a muchos de OrdenDeCompra a DetalleOrden
    //@OneToMany(mappedBy = "ordenDeCompra", cascade = CascadeType.ALL)
    @OneToMany(mappedBy = "ordenDeCompra", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DetalleOrden> detalles = new ArrayList<>();

    public OrdenDeCompra() {}

    public OrdenDeCompra(EstadoOrden estado, double total, LocalDate fechaEnvio, LocalDate fechaRecepcion, LocalDate fechaCreacion,String factura)
    {

        this.estado = estado;
        this.total = total;
        this.fechaEnvio = fechaEnvio;
        this.fechaRecepcion = fechaRecepcion;
        this.factura = factura;
        this.fechaCreacion = fechaCreacion;

    }


    // getters
    public Long getId(){return id;}
    public EstadoOrden getEstadoOrden(){return estado;}
    public double getTotal(){return total;}
    public LocalDate getFechaEnvio(){return fechaEnvio;}
    public LocalDate getFechaRecepcion(){return fechaRecepcion;}
    public LocalDate getFechaCreacion(){return fechaCreacion;}
    public String getFactura(){return factura;}


    public List<DetalleOrden> getDetalles() 
    {
        return detalles;
    }



    // setters
    
    public void setEstadoOrden(EstadoOrden estado){this.estado = estado;}
    public void setTotalOrdenCompra(double total){this.total = total;}
    public void setFechaEnvio(LocalDate fechaEnvio){this.fechaEnvio = fechaEnvio;}
    public void setFechaRecepcion(LocalDate fechaRecepcion){this.fechaRecepcion = fechaRecepcion;}
    public void setFechaCreacion(LocalDate fechaCreacion){this.fechaCreacion = fechaCreacion;}
    public void setFactura(String factura){this.factura = factura;}

    // este sólo lo usaré para el Test
    public void setId(Long id) {this.id = id;}

    public void agregarDetalle(DetalleOrden detalle) {
        this.detalles.add(detalle);
        detalle.setOrdenDeCompra(this); // Sincroniza la relación bidireccional
    }


}