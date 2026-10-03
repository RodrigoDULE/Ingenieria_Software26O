package mx.uam.ayd.proyecto.negocio.EntidadNegocio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


@Entity
public class DireccionEnvio {

    //Agrupa los datos capturados en el formulario de dirección.
    public static class DatosDireccion {
        private String nombreCompleto;
        private String calle;
        private String numero;
        private String colonia;
        private String ciudad;
        private String estado;
        private String codigoPostal;

        public DatosDireccion(String nombreCompleto, String calle, String numero, String colonia,
                String ciudad, String estado, String codigoPostal) {
            this.nombreCompleto = nombreCompleto;
            this.calle = calle;
            this.numero = numero;
            this.colonia = colonia;
            this.ciudad = ciudad;
            this.estado = estado;
            this.codigoPostal = codigoPostal;
        }

        public String getNombreCompleto() {
            return nombreCompleto;
        }
        public String getCalle() {
            return calle;
        }
        public String getNumero() {
            return numero;
        }
        public String getColonia() {
            return colonia;
        }
        public String getCiudad() {
            return ciudad;
        }
        public String getEstado() {
            return estado;
        }
        public String getCodigoPostal() {
            return codigoPostal;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDireccion;

    private String nombreCompleto;
    private String calle;
    private String numero;
    private String colonia;
    private String ciudad;
    private String estado;
    private String codigoPostal;

    /** Baja lógica: una dirección "eliminada" queda con activa=false, nunca se borra de la BD. */
    private boolean activa = true;

    /** Dueño de esta dirección. Lado ManyToOne: aquí vive la llave foránea hacia Cliente. */
    @ManyToOne
    private Cliente cliente;

    public DireccionEnvio() {
    }

    //Constructor principal: recibe los datos capturados en el formulario y el Cliente dueño de la dirección. 
    public DireccionEnvio(DatosDireccion datos, Cliente cliente) {
        this.nombreCompleto = datos.getNombreCompleto();
        this.calle = datos.getCalle();
        this.numero = datos.getNumero();
        this.colonia = datos.getColonia();
        this.ciudad = datos.getCiudad();
        this.estado = datos.getEstado();
        this.codigoPostal = datos.getCodigoPostal();
        this.cliente = cliente;
    }

    public long getIdDireccion() {
        return idDireccion;
    }
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    public String getCalle() {
        return calle;
    }
    public void setCalle(String calle) {
        this.calle = calle;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public String getColonia() {
        return colonia;
    }
    public void setColonia(String colonia) {
        this.colonia = colonia;
    }
    public String getCiudad() {
        return ciudad;
    }
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getCodigoPostal() {
        return codigoPostal;
    }
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
    public boolean isActiva() {
        return activa;
    }
    public void setActiva(boolean activa) {
        this.activa = activa;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}