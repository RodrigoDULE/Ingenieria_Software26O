package mx.uam.ayd.proyecto.negocio;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.repositorioCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Compra;

/**
 * Servicio Spring para gestionar operaciones sobre la entidad {@link Compra}.
 *
 * Ofrece métodos para crear/guardar una compra, recuperar una por id y listar
 * todas las compras.
 */
@Service
public class ServicioCompra {

	/** Repositorio inyectado para persistencia de compras. */
	private final repositorioCompra repoCompra;

	/**
	 * Constructor con inyección del repositorio.
	 *
	 * @param repoCompra repositorio utilizado para persistir {@link Compra}
	 */
	public ServicioCompra(repositorioCompra repoCompra){
		this.repoCompra = repoCompra;
	}

	/**
	 * Crea y persiste una nueva compra con el monto y la fecha indicados.
	 *
	 * @param Monto monto total de la compra
	 * @param fecha fecha de la compra
	 * @return la instancia de {@link Compra} persistida
	 */
	public Compra guardarCompra(float Monto,LocalDate fecha){
		Compra compra = new Compra();
		compra.setMonto(Monto);
		compra.setFecha(fecha);
		System.out.println("Compra guardada con éxito: " + compra.getIdCompra());
		return repoCompra.save(compra);  
	}

	/**
	 * Recupera una compra por su identificador.
	 *
	 * @param id identificador de la compra
	 * @return la {@link Compra} encontrada o {@code null} si no existe
	 */
	public Compra dameCompra(long id){
		return repoCompra.findByIdCompra(id);
	}

	/**
	 * Devuelve todas las compras almacenadas.
	 *
	 * @return iterable con todas las entidades {@link Compra}
	 */
	public Iterable<Compra> listaCompras(){
		return repoCompra.findAll();
	}

}
