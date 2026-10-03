package mx.uam.ayd.proyecto.negocio.EntidadNegocio;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;


/**
 * Entidad JPA que representa una compra realizada por un cliente.
 *
 * Contiene información básica de la compra como monto, dirección y fecha,
 * y la referencia al `carritoCompra` asociado (lado inverso de la relación).
 */
@Entity
public class Compra{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCompra;

    private float Monto;
    private String direccion;
    private LocalDate fecha;

    @OneToOne(mappedBy = "compra")
    private carritoCompra carrito;


    // Constructors

    /**
     * Constructor sin argumentos requerido por JPA.
     */
    public Compra() {
    }

    /**
     * Constructor de conveniencia para crear una compra con sus datos básicos.
     *
     * @param Monto    monto total de la compra
     * @param direccion dirección de envío asociada
     * @param fecha    fecha de la compra
     */
    public Compra(float Monto, String direccion, LocalDate fecha) {
        this.Monto = Monto;
        this.direccion = direccion;
        this.fecha = fecha;
    }

    // Setters y getters

    /**
     * Devuelve el identificador de la compra.
     *
     * @return id de la compra
     */
    public long getIdCompra(){
        return idCompra;
    }

    /**
     * Establece el identificador de la compra.
     *
     * @param idCompra id a asignar
     */
    public void setIdCompra(long idCompra){
        this.idCompra = idCompra;
    }

    /**
     * Devuelve el monto total de la compra.
     *
     * @return monto de la compra
     */
    public float getMonto(){
        return Monto;
    }

    /**
     * Establece el monto total de la compra.
     *
     * @param Monto monto a asignar
     */
    public void setMonto(float Monto){
        this.Monto = Monto;
    }

    /**
     * Devuelve la dirección de envío asociada a la compra.
     *
     * @return dirección de envío
     */
    public String getDireccion(){
        return direccion;
    }

    /**
     * Establece la dirección de envío de la compra.
     *
     * @param direccion dirección a asignar
     */
    public void setDireccion(String direccion){
        this.direccion = direccion;
    }

    /**
     * Devuelve la fecha en que se realizó la compra.
     *
     * @return fecha de la compra
     */
    public LocalDate getFecha(){
        return fecha;
    }

    /**
     * Establece la fecha de la compra.
     *
     * @param fecha fecha a asignar
     */
    public void setFecha(LocalDate fecha){
        this.fecha = fecha;
    }

    /**
     * Devuelve el carrito asociado a esta compra (lado inverso).
     *
     * @return carrito asociado o {@code null} si no existe
     */
    public carritoCompra getCarrito(){
        return carrito;
    }

    /**
     * Establece el carrito asociado a esta compra.
     *
     * @param carrito carrito a asociar
     */
    public void setCarrito(carritoCompra carrito){
        this.carrito = carrito;
    }

}
