package mx.uam.ayd.proyecto.negocio.EntidadNegocio;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class IntermediaCarritoProd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idIntermedia;

    private int cantidadTotalProd;

    //uncarrito tiene muchos productos onetomany

    @ManyToOne(targetEntity = carritoCompra.class, fetch = FetchType.EAGER)
    private carritoCompra car;

    @ManyToOne(targetEntity = Producto.class, fetch = FetchType.EAGER)
    private Producto prod;


    public long getIdIntermedia(){
        return idIntermedia;
    }

    public void setIdIntermedia(long idIntermedia){
        this.idIntermedia = idIntermedia;
    }

    public int getCantidadTotalProd(){
        return cantidadTotalProd;
    }

    public void setCantidadTotalProd(int cantidadTotalProd){
        this.cantidadTotalProd = cantidadTotalProd;
    }

    public carritoCompra getCarrito(){
        return car;
    }

    public void setCarrito(carritoCompra car){
        this.car = car;
    }

    public Producto getProd(){
        return prod;
    }

    public void setProd(Producto prod){
        this.prod = prod;
    }

}
