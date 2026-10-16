package mx.uam.ayd.proyecto.negocio.EntidadNegocio;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Producto {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idProducto;

    private String nombre;
    private float Precio;
    private String Descripcion;
    private int CantidadStock;
    private String tipoProd;
    private int contenidoMCL;
    private float GradoAlcohol;
    private String imagen;
    private String tipoAgave;
    private String region;
    private int volumen;



    
    public Producto(){}
    public Producto(String nombre, float precio, String Descripcion, int CantidadStock, int contenidoMCL, float GradoAlcohol, String tipoProd, String imagen){
        this.nombre = nombre;
        this.Precio = precio;
        this.Descripcion = Descripcion;
        this.CantidadStock = CantidadStock;
        this.tipoProd = tipoProd;
        this.contenidoMCL = contenidoMCL;
        this.GradoAlcohol = GradoAlcohol;
        this.imagen = imagen;
    }
    
    //Sobrecarga de constructor para nuevos atributos en catálogo (HU-01)
    public Producto(String nombre, float precio, String Descripcion, int CantidadStock, int contenidoMCL, float GradoAlcohol, String tipoProd, String imagen, int volumen, String tipoAgave, String region){
        this(nombre,precio,Descripcion,CantidadStock,contenidoMCL,GradoAlcohol,tipoProd,imagen);
        this.volumen = volumen;
        this.tipoAgave = tipoAgave;
        this.region = region;
    } 
    
    
    

    //Quiero sabe a que cliente le pertenece
    @ManyToOne(targetEntity = Cliente.class, fetch = FetchType.EAGER)
    private Cliente cliente;

    //cardinalidad con evento
    @ManyToOne
    private Evento evento;

    @OneToMany(mappedBy = "productoFK")
    private List<DetallesCompra> detalleCompra;

    //realizamos getters
    public long getidProducto(){return idProducto;}
    public String getnombre(){return nombre;}
    public float getPrecio(){return Precio;}
    public String getDescripcion(){return Descripcion;}
    public int getcantidadStock(){return CantidadStock;}
    public int getcontenidoMCL(){return contenidoMCL;}
    public float getGradoAlcohol(){ return GradoAlcohol;}
    public String getTipoProd(){ return tipoProd;}
    public String getRutaImagen(){ return imagen;}
    public String gettipoAgave(){ return tipoAgave;}
    public String getregion(){ return region;}
    public int getvolumen(){ return volumen;}
    
    
    //Solo es prueba para agregar dueño al producto
    public Cliente getingresarIdDueño(){return cliente;}
    public void setingresarIdDueño(Cliente cliente){this.cliente = cliente;}


    //realizamos setters

    public void setidProducto(long idProducto){this.idProducto = idProducto;}
    public void setnombre(String nombre){this.nombre = nombre;}
    public void setPrecio(float Precio){this.Precio = Precio;}
    public void setDescripcion(String Descripcion){this.Descripcion = Descripcion;}
    public void setcantidadStock(int CantidadStock){this.CantidadStock = CantidadStock;}
    public void setcontenidoMCL(int contenidoMCL){this.contenidoMCL = contenidoMCL;}
    public void setGradoAlcohol(float GradoAlcohol){ this.GradoAlcohol = GradoAlcohol;}
    public void setRutaImagen(String rutaImagen){ this.imagen = rutaImagen;}
    public void setTipoProd(String tipoProd){ this.tipoProd = tipoProd;}
    public Evento getEvento(){return evento;}
    public void setEvento(Evento evento){this.evento = evento;}
    public List<DetallesCompra> getDetalleCompra(){return detalleCompra;}
    public void setDetalleCompra(List<DetallesCompra> detalleCompra){this.detalleCompra = detalleCompra;}
    public void settipoAgave(String tipoAgave){this.tipoAgave=tipoAgave;}
    public void setregion(String region){ this.region=region;}
    public void setvolumen(int volumen){ this.volumen=volumen;}

    //generado **IA**
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Producto otroProducto = (Producto) obj;
        // Comparamos los IDs de los productos
        return this.idProducto == otroProducto.getidProducto(); 
    }

    @Override
    public int hashCode() {
        // Como tu ID seguramente es de tipo 'long', debes usar Long.hashCode()
        return Long.hashCode(this.idProducto); 
    }
}
