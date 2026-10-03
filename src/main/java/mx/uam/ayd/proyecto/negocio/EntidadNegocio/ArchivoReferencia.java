package mx.uam.ayd.proyecto.negocio.EntidadNegocio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

// Representa un archivo de referencia 
// que el usuario cargó como base para la generación de contenido (sección "Carga de Referencias" del configurador).
@Entity
public class ArchivoReferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Nombre  del archivo 
    private String nombre;
    // Extensión o tipo de archivo
    private String tipo;
    // Tamaño del archivo en bytes
    private Long tamanio;


    @ManyToOne
    private FormularioMarketing formularioMarketing;

    //Constructor vacío
    public ArchivoReferencia() {
    }

    //Constructor con la metadata del archivo cargado.

    public ArchivoReferencia(String nombre, String tipo, Long tamanio) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.tamanio = tamanio;
    }

    // --- Getters y setters ---
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getTamanio() {
        return tamanio;
    }

    public void setTamanio(Long tamanio) {
        this.tamanio = tamanio;
    }

    public FormularioMarketing getFormularioMarketing() {
        return formularioMarketing;
    }

    // Se usa desde FormularioMarketing.agregarArchivos() para mantener
    // consistente la relación en ambos sentidos.
    public void setFormularioMarketing(FormularioMarketing formularioMarketing) {
        this.formularioMarketing = formularioMarketing;
    }
}