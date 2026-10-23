package mx.uam.ayd.proyecto.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioResena;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Resena;
import jakarta.transaction.Transactional;
import java.util.List;


@Service
public class ServicioResena {

    @Autowired
    private RepositorioResena repositorioResena;
    //Metodo para guardar la reseña 


    /**
     * Valida y guarda una nueva resena para un producto.
     */
    @Transactional 
    public Resena crearResena(int calificacion, String titulo, String comentario, Producto producto) {
        
        // Regla de negocio 1: Validar que la calificación esté entre 1 y 5
        if (calificacion < 1 || calificacion > 5) {
            throw new IllegalArgumentException("La calificación debe ser de 1 a 5 estrellas.");
        }

        // Regla de negocio 2: Validar que el título no esté vacío
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la reseña no puede estar vacío.");
        }

        // Regla de negocio 3: Validar que el producto exista
        if (producto == null) {
            throw new IllegalArgumentException("La reseña debe estar asociada a un producto válido.");
        }

        // Creamos la entidad
        Resena nuevaResena = new Resena(calificacion, titulo, comentario, producto);

        // Guardamos usando el repositorio de Spring Boot
        return repositorioResena.save(nuevaResena);
    }

        // Método para consultar las reseñas de un producto específico
    public List<Resena> obtenerResenasPorProducto(Producto producto) {
        return repositorioResena.findByProducto(producto);
    }

}