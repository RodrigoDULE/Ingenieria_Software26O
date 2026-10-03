package mx.uam.ayd.proyecto.presentacion.HU09CalendarioDeEventos;

//importar paqueterias de listas
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

//librerias para escuchador
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Evento;
import mx.uam.ayd.proyecto.presentacion.HU02CarritoPrincipal.controladorCarritoPrincipal;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Empleado;




@Component
public class VistaCalendarioEventos {

    //cosas del archivo FXML
    @FXML
    private Button btnPrev;//<Button fx:id="btnPrev" text="◀"/>

    @FXML
    private Button btnNext;//<Label fx:id="lblMes" text="JUNIO 2026" style="-fx-font-size:20px;-fx-font-weight:bold;"/>
 

    @FXML
    private Label lblMes;//<Button fx:id="btnNext" text="▶"/>

    @FXML
    private GridPane calendarGrid;//<GridPane fx:id="calendarGrid" gridLinesVisible="true">

    @FXML
    private TextField txtBuscar;//<TextField fx:id="txtBuscar" promptText="Buscar evento..." HBox.hgrow="ALWAYS"/>

    @FXML
    private VBox panelEventos;//<VBox fx:id="panelEventos" spacing="10">
    


    private Stage stage;
    private boolean inicializado = false;
    private ControladorCalendarioEventos controlcalendario;
    
    private YearMonth mesActual;
    //mesActual = mesActual.plusMonths(1);
    //mesActual = mesActual.minusMonths(1);
  
    // constructor vacio
    public VistaCalendarioEventos() {
    }
    
    // inicializamos el controlador con un setter
    public void setControlador(ControladorCalendarioEventos calendario) {
        this.controlcalendario = calendario;
    }


    // UI
    private void inicializarUI() {
        if (inicializado) {
            return;
        }

        // crea UI solo si estamos en el hilo de JAVAFX
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::inicializarUI);
            return;
        }

        try {
            stage = new Stage();
            stage.setTitle("Calendario de Eventos");

            // Cargamos el fxml que tiene que ver con esta ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-calendario-evento.fxml"));
            loader.setController(this); // le estamos diciendo a javafx que esta clase es la que controla el fxml
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/css/estilos-mezicuil.css").toExternalForm());//Estilo css
            stage.setScene(scene);
            btnPrev.setOnAction(e -> mesAnterior());
            btnNext.setOnAction(e -> mesSiguiente());

            inicializado = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void mesAnterior() {
        mesActual = mesActual.minusMonths(1); // resta 1 mes a la fecha guardada
        dibujaCalendario();                   // vuelve a pintar los botones
        actualizarTituloMes();                // cambia el texto del ñabel 
    }

    public void mesSiguiente() {
        mesActual = mesActual.plusMonths(1);  // suma 1 mes
        dibujaCalendario();
        actualizarTituloMes();
    }

    public void actualizarTituloMes() {
        // Formatea "2026-06" a "JUNIO 2026"
        String nombreMes = mesActual.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase();
        lblMes.setText(nombreMes + " " + mesActual.getYear());
    }
    

    public void dibujaCalendario(){
        LocalDate primerDia = mesActual.atDay(1);
        
        int diasMes= mesActual.lengthOfMonth();
        int fila=0;
        int columnaInicial= primerDia.getDayOfWeek().getValue();;
        
        if(columnaInicial==7){

            columnaInicial=0;
        }
        int columna = columnaInicial;
        calendarGrid.getChildren().clear();;

        for(int dia=1; dia <= diasMes; dia++){
            
            Button boton = new Button(Integer.toString(dia));
            calendarGrid.add(boton, columna, fila);
            //mesActual.atDay(dia);
            
            if(columna==7){
                fila++;
                columna=0;
            }
            LocalDate fecha = mesActual.atDay(dia);
            columna++;
            boton.setOnAction(event -> controlcalendario.seleccionarFecha(fecha));
        }


    }

    public void muestraCalendario() {
        inicializarUI(); 
        mesActual = YearMonth.now();
    

        dibujaCalendario();
    

        actualizarTituloMes();
        if (stage != null) {
            stage.show();
        }
    }
    
    public void mostrarEventos(List <Evento> eventos){
        panelEventos.getChildren().clear();
        for(Evento evento: eventos){
            VBox tarjeta = new VBox();
            Label nombre = new Label(evento.getNombreEvento());
            Label lugar = new Label(evento.getLugar());
            Label horaIn = new Label(evento.getHoraIn().toString());
            Label horaFin = new Label(evento.getHoraFin().toString());
            Label Comision = new Label(String.valueOf(evento.getComision()));
            Button editar = new Button("Editar");

            editar.setOnAction(e -> {
                if (controlcalendario != null) {
                    controlcalendario.editarNotificacionEvento(evento);
                }else{

                    System.err.println("❌ ERROR: 'controlcalendario' es NULL en la Vista");
                }
            });


            List<Empleado> listaEmpleados = evento.getEmpleados();
            String nombres = "";
            if (listaEmpleados != null && !listaEmpleados.isEmpty()) {
                for (Empleado emp : listaEmpleados) {
                    nombres += emp.getNombreEmpleado() + ", ";
                }
                // Quita la última coma y espacio sobrante
                nombres = nombres.substring(0, nombres.length() - 2);
            } else {
                nombres = "Sin empleados asignados";
            }

            Label lblEmpleados = new Label("Empleados: " + nombres);


            
            tarjeta.getChildren().addAll(nombre, lugar, horaIn, horaFin, Comision, lblEmpleados, editar);
            panelEventos.getChildren().add(tarjeta);
        }

    }


    
}








