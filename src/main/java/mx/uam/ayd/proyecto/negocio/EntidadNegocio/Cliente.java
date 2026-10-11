package mx.uam.ayd.proyecto.negocio.EntidadNegocio;

import java.util.List;

import jakarta.persistence.Entity;

import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCliente;

    private String nombre;
    private int edad;

    public Cliente(String nombre, int edad){
        this.nombre = nombre;
        this.edad = edad;
    }

    //cada cliente le pertenece cero o un carrito, dentro de la tabla de cliente se agregara una coluna especial llamda carrito_id, puede ser nulo o uno, es como una llave foranea
    @OneToOne(targetEntity = carritoCompra.class, fetch = FetchType.EAGER)
    private carritoCompra carrito;

    // HU-05: cada cliente puede tener cero o una direccion predeterminada
    @OneToOne(targetEntity = DireccionEnvio.class, fetch = FetchType.EAGER)
    private DireccionEnvio direccionPredeterminada;

    @OneToMany(mappedBy = "clienteFK")
    private List<Compra> compraFK;

    //Inicializamos un constructor vacio
    public Cliente(){}

    public long getidCliente(){return idCliente;}
    public void setidCliente(long idCliente){this.idCliente = idCliente;}
    public String getNombre(){return nombre;}
    public void setNombre(String nombre){this.nombre = nombre;}
    public int getEdad(){return edad;}
    public void setEdad(int edad){this.edad = edad;}
    public carritoCompra getCarritoCompra(){return carrito;}

    //agregamos un setter para agregarle un carrito a la posesion del cliente
    public void setcarritoCompra(carritoCompra carrito){this.carrito = carrito;}

    public DireccionEnvio getDireccionPredeterminada(){return direccionPredeterminada;}
    public void setDireccionPredeterminada(DireccionEnvio direccion){this.direccionPredeterminada = direccion;}

    public List<Compra> getCompraFK(){return compraFK;}
    public void setCompraFK(List<Compra> compraFK){this.compraFK = compraFK;}

}