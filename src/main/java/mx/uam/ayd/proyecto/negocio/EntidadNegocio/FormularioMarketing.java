package mx.uam.ayd.proyecto.negocio.EntidadNegocio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class FormularioMarketing {

    //opciones de contenido
    public enum TipoContenido {
        SOLO_TEXTO,
        IMAGEN_ESTATICA
    }

    // Agrupa los datos capturados en el formulario (todo excepto los archivos, que se manejan aparte).
    public static class DatosFormulario {

        @Enumerated(EnumType.STRING)
        private TipoContenido tipoContenido;
        @ElementCollection
        private List<String> plataformasDestino;
        private Integer cantidadVariaciones;
        private LocalDate fechaEstimadaPublicacion;
         

        public DatosFormulario(TipoContenido tipoContenido, List<String> plataformasDestino,
                Integer cantidadVariaciones, LocalDate fechaEstimadaPublicacion) {
            this.tipoContenido = tipoContenido;
            this.plataformasDestino = plataformasDestino;
            this.cantidadVariaciones = cantidadVariaciones;
            this.fechaEstimadaPublicacion = fechaEstimadaPublicacion;
        }

        public TipoContenido getTipoContenido() {
            return tipoContenido;
        }

        public List<String> getPlataformasDestino() {
            return plataformasDestino;
        }

        public Integer getCantidadVariaciones() {
            return cantidadVariaciones;
        }

        public LocalDate getFechaEstimadaPublicacion() {
            return fechaEstimadaPublicacion;
        }

        
    }

    
    
    
    // (clave primaria)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;


    private TipoContenido tipoContenido;
    
    @ElementCollection
    private List<String> plataformasDestino = new ArrayList<>();

    private Integer cantidadVariaciones;
    private LocalDate fechaEstimadaPublicacion;

    // Archivos de referencia que el usuario cargó como base para la
    // generación
    @OneToMany(mappedBy = "formularioMarketing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArchivoReferencia> archivos = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idPublicacion")
    private PublicacionMarketing publicacion;
    

    //Constructor Vacío
    public FormularioMarketing() {
    }

    
    //recibe los datos ya empaquetados en un DatosFormulario y la lista de archivos ya convertidos 
    // a ArchivoReferencia. 
    public FormularioMarketing(DatosFormulario datos, List<ArchivoReferencia> archivos) {
        this.tipoContenido = datos.getTipoContenido();
        this.plataformasDestino = datos.getPlataformasDestino();
        this.cantidadVariaciones = datos.getCantidadVariaciones();
        this.fechaEstimadaPublicacion = datos.getFechaEstimadaPublicacion();
        agregarArchivos(archivos);
    }

    //Asocia una lista de archivos a este formulario.
    public void agregarArchivos(List<ArchivoReferencia> nuevosArchivos) {
        if (nuevosArchivos == null) {
            return;
        }
        for (ArchivoReferencia archivo : nuevosArchivos) {
            archivo.setFormularioMarketing(this);
            this.archivos.add(archivo);
        }
    }

    
    @Override
public String toString(){
    return nombre;
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

    public TipoContenido getTipoContenido() {
        return tipoContenido;
    }

    public void setTipoContenido(TipoContenido tipoContenido) {
        this.tipoContenido = tipoContenido;
    }

    public List<String> getPlataformasDestino() {
        return plataformasDestino;
    }

    public void setPlataformasDestino(List<String> plataformasDestino) {
        this.plataformasDestino = plataformasDestino;
    }

    public Integer getCantidadVariaciones() {
        return cantidadVariaciones;
    }

    public void setCantidadVariaciones(Integer cantidadVariaciones) {
        this.cantidadVariaciones = cantidadVariaciones;
    }

    public LocalDate getFechaEstimadaPublicacion() {
        return fechaEstimadaPublicacion;
    }

    public void setFechaEstimadaPublicacion(LocalDate fechaEstimadaPublicacion) {
        this.fechaEstimadaPublicacion = fechaEstimadaPublicacion;
    }

    public List<ArchivoReferencia> getArchivos() {
        return archivos;
    }


    public PublicacionMarketing getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(PublicacionMarketing publicacion) {
        this.publicacion = publicacion;
    }
}