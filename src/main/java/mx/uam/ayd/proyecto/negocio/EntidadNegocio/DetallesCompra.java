package mx.uam.ayd.proyecto.negocio.EntidadNegocio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity 
public class DetallesCompra {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetallesCompra;

    //agregamos las cardinalidades
    @ManyToOne
    @JoinColumn (name = "compraFK")  
    private Compra compraFK;
    
    @ManyToOne  
    @JoinColumn (name = "productoFK")  
    private Producto productoFK;

    private int cantidadComprada;
    private double precioProducto;

    public DetallesCompra() {
    }

    public DetallesCompra(Compra compraFK, Producto productoFK, int cantidadComprada, double precioProducto) {
        this.compraFK = compraFK;
        this.productoFK = productoFK;
        this.cantidadComprada = cantidadComprada;
        this.precioProducto = precioProducto;
    }

    public Long getIdDetallesCompra() {
        return idDetallesCompra;
    }

    public void setIdDetallesCompra(Long idDetallesCompra) {
        this.idDetallesCompra = idDetallesCompra;
    }

    public Compra getCompraFK() {
        return compraFK;
    }

    public void setCompraFK(Compra compraFK) {
        this.compraFK = compraFK;
    }

    public Producto getProductoFK() {
        return productoFK;
    }

    public void setProductoFK(Producto productoFK) {
        this.productoFK = productoFK;
    }

    public int getCantidadComprada() {
        return cantidadComprada;
    }

    public void setCantidadComprada(int cantidadComprada) {
        this.cantidadComprada = cantidadComprada;
    }

    public double getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(double precioProducto) {
        this.precioProducto = precioProducto;
    }
}
