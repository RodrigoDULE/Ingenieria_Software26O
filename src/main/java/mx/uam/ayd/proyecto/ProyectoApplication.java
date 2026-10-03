package mx.uam.ayd.proyecto;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.datos.repositorioCliente;
import mx.uam.ayd.proyecto.datos.repositorioProducto;
import mx.uam.ayd.proyecto.datos.RepositorioEvento;
import mx.uam.ayd.proyecto.datos.RepositorioEmpleado;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Evento;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Empleado;

import mx.uam.ayd.proyecto.presentacion.Principal.controladorPrincipal;

/**
 * 
 * Clase principal que arranca la aplicación
 * construida usando el principio de
 * inversión de control
 * Adaptada para usar JavaFX
 * 
 * @author Humberto Cervantes (c) 21 Nov 2022
 */
@SpringBootApplication
public class ProyectoApplication {

	private final controladorPrincipal controlPrincipal;
	private final repositorioProducto repositorioProd;
	private final repositorioCliente repositorioCliente;
	private final RepositorioEvento repositorioEvento;
	private final RepositorioEmpleado repositorioEmpleado;

	@Autowired
	public ProyectoApplication(controladorPrincipal controlPrincipal, repositorioProducto repositorioProd,
			repositorioCliente repositorioCliente, RepositorioEvento repositorioEvento,
			RepositorioEmpleado repositorioEmpleado) {
		this.controlPrincipal = controlPrincipal;
		this.repositorioProd = repositorioProd;
		this.repositorioCliente = repositorioCliente;
		this.repositorioEvento = repositorioEvento;
		this.repositorioEmpleado = repositorioEmpleado;
	}

	/**
	 * Método principal
	 *
	 * @param args argumentos de la línea de comando
	 */
	public static void main(String[] args) {
		// Launch JavaFX application
		Application.launch(JavaFXApplication.class, args);
	}

	/**
	 * Clase interna para manejar la inicialización de JavaFX
	 */
	public static class JavaFXApplication extends Application {

		private static ConfigurableApplicationContext applicationContext;

		@Override
		public void init() throws Exception {
			// Create Spring application context
			SpringApplicationBuilder builder = new SpringApplicationBuilder(ProyectoApplication.class);
			builder.headless(false);
			applicationContext = builder.run(getParameters().getRaw().toArray(new String[0]));
		}

		@Override
		public void start(Stage primaryStage) {
			// Initialize the application on the JavaFX thread
			Platform.runLater(() -> {
				applicationContext.getBean(ProyectoApplication.class).inicia();
			});
		}

		@Override
		public void stop() throws Exception {
			applicationContext.close();
			Platform.exit();
		}
	}

	/**
	 * Metodo que arranca la aplicacion
	 * inicializa la bd y arranca el controlador
	 */
	public void inicia() {
		inicializaBD();
		inicializaBDUsaurio();

		// Make sure controllers are created on JavaFX thread
		Platform.runLater(() -> {
			controlPrincipal.inicia();
		});
	}

	public void inicializaBDUsaurio() {
		repositorioCliente.save(new Cliente("Valeria", 20));
		repositorioCliente.save(new Cliente("Jose", 21));
		repositorioCliente.save(new Cliente("Leonardo", 23));
		repositorioCliente.save(new Cliente("Jean", 22));
		repositorioCliente.save(new Cliente("Rodrigo", 27));
		repositorioCliente.save(new Cliente("Humberto", 40));
	}

	/**
	 * Inicializa la BD con datos
	 */
	public void inicializaBD() {
		// Vamos a crear los dos grupos de usuarios

		Producto prod = new Producto();
		prod.setnombre("Lista de Productos Disponibles");

		repositorioEvento
				.save(new Evento("Mezcaleria Coapa", "Casa Mezcal", 500, 4, LocalDate.of(2026, 9, 24),
						LocalTime.of(10, 0), LocalTime.of(14, 0), LocalDate.of(2026, 7, 20), "Conferencia", "pago",
						"Lluvia de ideas sobre mezcal"));
		repositorioEvento
				.save(new Evento("San Martin", "Casa Mezcal", 500, 3, LocalDate.of(2026, 8, 25),
						LocalTime.of(10, 0), LocalTime.of(14, 0), LocalDate.of(2026, 7, 20), "Reunion", "pago",
						"Lluvia de ideas sobre mezcal"));
		repositorioEvento
				.save(new Evento("Cumpleaños", "Casa Mezcal", 500, 2, LocalDate.of(2026, 7, 29),
						LocalTime.of(10, 0), LocalTime.of(14, 0), LocalDate.of(2026, 7, 20), "Taller", "pago",
						"Lluvia de ideas sobre mezcal"));

		repositorioProd
				.save(new Producto("Mezcal Espadin Joven", 450.0f, "Mezcal artesanal notas citricas y ahumado suave",
						50, 750, 40.0f, "Mezcal", "/Imagenes/Mezcales/mz1.jpg"));
		repositorioProd.save(new Producto("Mezcal Reposado con Gusano", 550.0f,
				"Reposado 6 meses en barrica de roble con gusano de maguey", 30, 750, 38.0f, "Mezcal",
				"/Imagenes/Mezcales/mz2.jpg"));
		repositorioProd
				.save(new Producto("Mezcal Anejo Reserva", 850.0f, "Anejado por 14 meses, notas de vainilla y madera",
						20, 750, 40.0f, "Mezcal", "/Imagenes/Mezcales/mz3.jpg"));
		repositorioProd.save(new Producto("Mezcal Tobala Silvestre", 1100.0f,
				"Elaborado con agave silvestre, sabor complejo y floral", 15, 750, 42.0f, "Mezcal",
				"/Imagenes/Mezcales/mz4.jpg"));
		repositorioProd
				.save(new Producto("Mezcal Tepeztate", 1250.0f, "Agave madurado por 25 anos, notas herbales intensas",
						10, 750, 45.0f, "Mezcal", "/Imagenes/Mezcales/mz5.png"));
		repositorioProd.save(new Producto("Mezcal Artesanal de Pechuga", 1500.0f,
				"Doble destilación con pechuga de pavo y frutas de temporada", 5, 750, 46.0f,
				"Mezcal", "/Imagenes/Mezcales/mz6.png"));

		// Cremas de Mezcal (Menor grado de alcohol)
		repositorioProd.save(new Producto("Crema de Mezcal sabor Cafe", 250.0f,
				"Deliciosa crema dulce con toque intenso a cafe tostado", 40, 750, 15.0f, "Mezcal",
				"/Imagenes/Mezcales/mz7.png"));
		repositorioProd.save(new Producto("Crema de Mezcal sabor Pinon", 280.0f,
				"Crema dulce con autentico sabor a piñón rosa", 35, 750, 15.0f, "Mezcal",
				"/Imagenes/Mezcales/mz8.png"));
		repositorioProd.save(new Producto("Crema de Mezcal sabor Fresa", 250.0f,
				"Licor cremoso ideal para postres o digestivo", 45, 750, 15.0f, "Mezcal",
				"/Imagenes/Mezcales/mz9.png"));

		// Complementos y Accesorios (0 grado de alcohol)
		// Imagen 1: Vaso Veladora Clásico
		repositorioProd.save(new Producto("Vaso Veladora Mezcalero Clásico", 45.0f,
				"Vaso tradicional de vidrio acanalado con base de cruz, ideal para degustar mezcal",
				60, 60, 0.0f, "Complementos", "/Imagenes/Complementos/ct1.jpg"));

		// Imagen 2: Estuche de Presentación
		repositorioProd.save(new Producto("Estuche de Presentación Agave Azul", 250.0f,
				"Caja decorativa de edición especial con ventana circular y arte de corazón de agave",
				25, 0, 0.0f, "Complementos", "/Imagenes/Complementos/ct2.png"));

		// Imagen 3: Figura Alebrije Ajolote
		repositorioProd.save(new Producto("Figura Alebrije Ajolote Místico", 320.0f,
				"Pieza artesanal tallada y pintada a mano inspirada en un ajolote con base brillante",
				15, 0, 0.0f, "Complementos", "/Imagenes/Complementos/ct3.png"));

		// Imagen 4: Botella Decorativa Tlacuache
		repositorioProd.save(new Producto("Botella Decorativa Alebrije Tlacuache", 450.0f,
				"Botella artesanal adornada con una figura de tlacuache estilo alebrije tallada a mano",
				10, 750, 0.0f, "Complementos", "/Imagenes/Complementos/ct4.png"));

		// Imagen 5: Miniatura Alebrije Dragón
		repositorioProd.save(new Producto("Miniatura Edición Alebrije Dragón", 380.0f,
				"Figura artesanal en tonos naranja y violeta abrazando una botella de degustación",
				20, 50, 0.0f, "Complementos", "/Imagenes/Complementos/ct5.png"));

		// Imagen 6: Alebrije Serpiente Alada
		repositorioProd.save(new Producto("Adorno Alebrije Serpiente Alada", 400.0f,
				"Alebrije multicolor de serpiente alada diseñado para sujetarse al cuello de la botella",
				15, 750, 0.0f, "Complementos", "/Imagenes/Complementos/ct6.png"));

		// Empleados
		repositorioEmpleado.save(new Empleado("Jorge Medina", "555-555-5555"));
		repositorioEmpleado.save(new Empleado("Luis Angel", "555-555-5555"));
		repositorioEmpleado.save(new Empleado("Diego Reyes", "555-555-5555"));
		repositorioEmpleado.save(new Empleado("Saul Lopez", "555-555-5555"));
		repositorioEmpleado.save(new Empleado("Jose Maya", "555-555-5555"));
		repositorioEmpleado.save(new Empleado("Juan Perez", "555-555-5555"));
		repositorioEmpleado.save(new Empleado("Pedro Mendoza", "555-555-5555"));

	}
}
