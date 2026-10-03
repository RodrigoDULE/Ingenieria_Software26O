package mx.uam.ayd.proyecto.negocio;
import java.util.List;

//leo D
import org.springframework.stereotype.Service;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.FormularioMarketing;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.PublicacionMarketing;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.ArchivoReferencia;
import mx.uam.ayd.proyecto.datos.RepositorioFormularioMarketing;
import mx.uam.ayd.proyecto.datos.RepositorioPublicacionMarketing;
import mx.uam.ayd.proyecto.datos.RepositorioArchivoReferencia;
import mx.uam.ayd.proyecto.datos.RepositorioEvento;


@Service
public class ServicioGuardarContenido {

    
    //constructor 
    private RepositorioArchivoReferencia repoArchivoReferencia;
    private RepositorioFormularioMarketing repoFormularioMarketing;
    private RepositorioPublicacionMarketing repoPublicacion;
    public ServicioGuardarContenido(RepositorioFormularioMarketing repoFormularioMarketing, RepositorioArchivoReferencia repoArchivoReferencia, RepositorioPublicacionMarketing repoPublicacion){
        this.repoFormularioMarketing = repoFormularioMarketing;
        this.repoArchivoReferencia = repoArchivoReferencia;
        this.repoPublicacion= repoPublicacion;

    }


    
    public FormularioMarketing guardarFormulario(FormularioMarketing formularioMarketing){
        if(formularioMarketing==null){
            throw new IllegalArgumentException();
        }
        
        //validar nombre
        if(formularioMarketing.getNombre() == null ||formularioMarketing.getNombre().trim().isEmpty()){
            throw new IllegalArgumentException();
        }

        return repoFormularioMarketing.save(formularioMarketing);
    }

   
//ontener tyodos los formularios
    public List<FormularioMarketing> obtenerFormularios(){

        return (List<FormularioMarketing>) repoFormularioMarketing.findAll();
    }
    
    
    
    //buscar UN SOLO formulario
    
    
    public FormularioMarketing buscarFormulario(long idFormulario){

        return repoFormularioMarketing.findById(idFormulario);
    }

    
    
    public FormularioMarketing actualizarFormulario(FormularioMarketing formulario){
        if (formulario==null) {
            throw new IllegalArgumentException();
        }

        //valida el nombre 
        if(formulario.getNombre() == null ||formulario.getNombre().trim().isEmpty()){
            throw new IllegalArgumentException("poner nombre :(");
        }


        return repoFormularioMarketing.save(formulario);
    }

    




    public PublicacionMarketing publicarFormulario(long idFormulario,PublicacionMarketing publicacion){

        FormularioMarketing formulario =
            repoFormularioMarketing.findById(idFormulario);

        if(formulario == null){
            throw new IllegalArgumentException();
        }

        if(formulario.getPublicacion() != null){
            throw new IllegalArgumentException();
        }


        //valida la plataforma 
        if(formulario.getPlataformasDestino() == null ||formulario.getPlataformasDestino().isEmpty()){
            throw new IllegalArgumentException();
        }

        formulario.setPublicacion(publicacion);

        publicacion.setFormularioMarketing(formulario);

        repoFormularioMarketing.save(formulario);

        return publicacion;
    }

}
