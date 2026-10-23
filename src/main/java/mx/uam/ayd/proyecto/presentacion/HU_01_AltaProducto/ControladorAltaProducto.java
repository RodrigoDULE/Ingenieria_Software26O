package mx.uam.ayd.proyecto.presentacion.HU_01_AltaProducto;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import jakarta.annotation.PostConstruct;
import javafx.scene.control.Alert.AlertType;
import mx.uam.ayd.proyecto.negocio.servicioProducto;

/**
 * Controlador coordinador para la HU-01: Gestión de Inventario (Alta de Producto).
 */
@Controller
public class ControladorAltaProducto {

    @Autowired
    private vistaAltaProducto vista;

    @Autowired
    private servicioProducto servProd;

    @PostConstruct
    public void init() {
        vista.setControlador(this);
    }

    public void inicia() {
        vista.muestra();
    }

    /**
     * Valida y registra el producto en negocio, copiando la imagen seleccionada al proyecto.
     */
    public void guardarProducto(String nombre, float precio, String descripcion,
                                int stock, int contenido, float grados,
                                String categoria, String rutaRelativaImagen,
                                String agave, String region, File archivoImagenOrigen) {
        try {
            // 1. Primero validamos y guardamos en la capa de negocio (Fail-Fast: Escenarios 2, 3 y 4)
            servProd.agregarProducto(nombre, precio, descripcion, stock, contenido,
                                     grados, categoria, rutaRelativaImagen, agave, region);

            // 2. Si las reglas de negocio pasaron sin excepción, copiamos la imagen a los recursos
            copiarImagenAlProyecto(archivoImagenOrigen);

            // 3. Notificamos éxito y limpiamos el formulario (Escenario 1)
            vista.mostrarMensaje("Éxito", "El producto y su fotografía se han guardado correctamente en el catálogo.", AlertType.INFORMATION);
            vista.limpiarCampos();

        } catch (IllegalArgumentException e) {
            vista.mostrarMensaje("Datos inválidos", e.getMessage(), AlertType.WARNING);
        } catch (IOException e) {
            vista.mostrarMensaje("Error de archivo", "El producto se registró, pero ocurrió un error al copiar la fotografía: " + e.getMessage(), AlertType.ERROR);
        }
    }

    /**
     * Copia el archivo de imagen desde la PC del usuario hacia src/main/resources/Imagenes
     * y hacia target/classes/Imagenes para que el catálogo lo renderice de inmediato.
     */
    private void copiarImagenAlProyecto(File archivoOrigen) throws IOException {
        if (archivoOrigen == null || !archivoOrigen.exists()) {
            throw new IllegalArgumentException("Debe seleccionar un archivo de imagen válido.");
        }

        String nombreArchivo = archivoOrigen.getName().replaceAll("\\s+", "_");

        // 1. Copia persistente en el código fuente del proyecto (src/main/resources/Imagenes)
        Path directorioSrc = Paths.get("src", "main", "resources", "Imagenes");
        Files.createDirectories(directorioSrc);
        Path destinoSrc = directorioSrc.resolve(nombreArchivo);
        Files.copy(archivoOrigen.toPath(), destinoSrc, StandardCopyOption.REPLACE_EXISTING);

        // 2. Copia en el Classpath activo en tiempo de ejecución (target/classes/Imagenes)
        Path directorioTarget = Paths.get("target", "classes", "Imagenes");
        Files.createDirectories(directorioTarget);
        Path destinoTarget = directorioTarget.resolve(nombreArchivo);
        Files.copy(archivoOrigen.toPath(), destinoTarget, StandardCopyOption.REPLACE_EXISTING);
    }
}