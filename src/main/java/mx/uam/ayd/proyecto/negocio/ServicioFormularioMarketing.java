package mx.uam.ayd.proyecto.negocio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioFormularioMarketing;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.ArchivoReferencia;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.FormularioMarketing;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.FormularioMarketing.DatosFormulario;


// Actúa como intermediario entre la capa de presentación (Controlador) y el acceso a datos.
@Service
public class ServicioFormularioMarketing {

    private final RepositorioFormularioMarketing repositorioFormularioMarketing;

    // Constructor para inyección de dependencias del repositorio.
    public ServicioFormularioMarketing(RepositorioFormularioMarketing repositorioFormularioMarketing) {
        this.repositorioFormularioMarketing = repositorioFormularioMarketing;
    }

    // Delega la extracción de metadatos de los archivos, ensambla la entidad 
    // principal (FormularioMarketing) con los datos capturados y la persiste en la base de datos.
    public FormularioMarketing registrarFormulario(DatosFormulario datos, List<File> archivos) {

        List<ArchivoReferencia> instanciasArchivos = crearArchivosReferencia(archivos);

        FormularioMarketing formulario = new FormularioMarketing(datos, instanciasArchivos);

        return repositorioFormularioMarketing.save(formulario);
    }

    // Transforma cada archivo cargado por el usuario en una entidad referencial (ArchivoReferencia).
    // Conserva únicamente (nombre, tipo, tamaño) 
    private List<ArchivoReferencia> crearArchivosReferencia(List<File> archivos) {
        List<ArchivoReferencia> resultado = new ArrayList<>();
        if (archivos == null) {
            return resultado;
        }
        for (File archivo : archivos) {
            String nombre = archivo.getName();
            String tipo = obtenerExtension(nombre);
            Long tamanio = archivo.length();
            resultado.add(new ArchivoReferencia(nombre, tipo, tamanio));
        }
        return resultado;
    }

    // Extrae de forma segura la extensión de un archivo (ej. "pdf", "docx") basándose en su nombre.
    private String obtenerExtension(String nombreArchivo) {
        int idx = nombreArchivo.lastIndexOf('.');
        if (idx == -1 || idx == nombreArchivo.length() - 1) {
            return "";
        }
        return nombreArchivo.substring(idx + 1).toLowerCase();
    }
}