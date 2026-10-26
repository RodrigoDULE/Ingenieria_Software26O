package mx.uam.ayd.proyecto.negocio.EntidadNegocio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn; 
@Entity 
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int calificacion; // De 1 a 5 estrellas
    private String titulo;
    private String comentario;
    // Relacion: Muchas reseñas pertenecen a un producto
    @ManyToOne
    @JoinColumn(name="producto_id")
    private Producto producto; 
    //Relacion: Muchas reseñas pertenecen a un usuario 
    @ManyToOne
    @JoinColumn(name="cliente_id")
    private Cliente cliente; 
    // Constructor vacío obligatorio para JPA
    public Resena() {
    }

    public Resena(int calificacion, String titulo, String comentario, Producto producto) {
        this.calificacion = calificacion;
        this.titulo = titulo;
        this.comentario = comentario;
        this.producto = producto;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getCalificacion() { return calificacion; }
    public void setCalificacion(int calificacion) { this.calificacion = calificacion; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}

