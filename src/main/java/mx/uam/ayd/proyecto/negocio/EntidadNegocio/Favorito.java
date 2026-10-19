package mx.uam.ayd.proyecto.negocio.EntidadNegocio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Favorito {

    // Identificador de cada registro de favorito
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFavorito;

    // Cada favorito corresponde a un cliente
    // Entonces JPA guarda en esta columna el id del cliente
    @ManyToOne
    @JoinColumn(name = "idClienteFK")
    private Cliente clienteFK;

    // Cada favorito corresponde a un producto
    //  JPA guarda en esta columna el Id del producto
    @ManyToOne
    @JoinColumn(name = "idProductoFK")
    private Producto productoFK;

    //    un constructor sin argumentos que necesita JPA, para crear la entidad al leerla de la base 
    public Favorito() {
    }

    public Long getIdFavorito() {
        return idFavorito;
    }

    public void setIdFavorito(Long idFavorito) {
        this.idFavorito = idFavorito;
    }

    public Cliente getClienteFK() {
        return clienteFK;
    }

    public void setClienteFK(Cliente clienteFK) {
        this.clienteFK = clienteFK;
    }

    public Producto getProductoFK() {
        return productoFK;
    }

    public void setProductoFK(Producto productoFK) {
        this.productoFK = productoFK;
    }
}
