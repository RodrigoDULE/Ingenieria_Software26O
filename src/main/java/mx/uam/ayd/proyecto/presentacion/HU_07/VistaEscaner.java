package mx.uam.ayd.proyecto.presentacion.HU_07;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.DetalleOrden;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra;

@Component
public class VistaEscaner {

    private ControladorEscaner controlador;
    private Stage stageActual;
    private OrdenDeCompra ordenActual;

    @FXML private TextField txtEscaner;
    @FXML private Button btnConfirmarIngreso;
    @FXML private TableView<DetalleOrden> tablaProductos;
    @FXML private TableColumn<DetalleOrden, String> colInsumo;
    @FXML private TableColumn<DetalleOrden, String> colSku;
    @FXML private TableColumn<DetalleOrden, String> colCantidad;
    @FXML private TableColumn<DetalleOrden, String> colEstado;

    public VistaEscaner(){}

    public void setControlador(ControladorEscaner controlador)
    {
        this.controlador = controlador;
    }



    public void muestraEscaner(OrdenDeCompra orden)
    {

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestraEscaner(orden));
            return;
        }

        this.stageActual = new Stage();
        this.ordenActual = orden;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana_Escaner.fxml")); 
            loader.setController(this); 
            
            Scene nuevaEscena = new Scene(loader.load()); 
            stageActual.setScene(nuevaEscena);
            stageActual.setTitle("Escaneo de Productos");
            
            btnConfirmarIngreso.setDisable(true);

            configurarTabla();
            
            stageActual.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void registrarEscaneo() {
        String codigo = txtEscaner.getText().trim();
        
        if (!codigo.isEmpty()) {
            // Le pedimos al controlador que haga el cálculo
            boolean exito = controlador.procesarEscaneo(codigo, ordenActual);
            
            if (exito) {
                tablaProductos.refresh(); // actualizamos los números en pantalla (las cantidades escaneadas)
                
                // Si la orden ya está completa, desbloqueamos el botón verde
                if(controlador.verificarOrdenCompleta(ordenActual)) {
                    btnConfirmarIngreso.setDisable(false);
                    txtEscaner.setDisable(true); // Bloqueamos el escáner para evitar errores
                }
            } else {
                System.out.println("Producto no correspondiente o ya está completo.");
            }
        }
        
        // Limpiamos el campo para poder hacer otro escaneo
        txtEscaner.clear();
        txtEscaner.requestFocus();
    }

    private void configurarTabla() {
        
        colInsumo.setCellValueFactory(cellData -> {
            String nombre = cellData.getValue().getInsumo().getNombre();
            return new javafx.beans.property.SimpleStringProperty(nombre);
        });

        colSku.setCellValueFactory(cellData -> {
            String sku = cellData.getValue().getInsumo().getSku();
            return new javafx.beans.property.SimpleStringProperty(sku);
        });

        colCantidad.setCellValueFactory(cellData -> {
            DetalleOrden detalle = cellData.getValue();
            String textoCantidad = detalle.getCantidadEscaneada() + " / " + detalle.getCantidad();
            return new javafx.beans.property.SimpleStringProperty(textoCantidad);
        });

        colEstado.setCellValueFactory(cellData -> {
            DetalleOrden detalle = cellData.getValue();
            
            if (detalle.getCantidadEscaneada() == detalle.getCantidad()) {
                return new javafx.beans.property.SimpleStringProperty("Validado");
            } else {
                return new javafx.beans.property.SimpleStringProperty("Escaneando");
            }
        });

        if (ordenActual != null && ordenActual.getDetalles() != null) {
            tablaProductos.getItems().setAll(ordenActual.getDetalles());
        }
    }

    @FXML
    public void finalizarIngreso() {
        controlador.confirmarIngresoAlmacen(ordenActual);
    }

    @FXML
    public void cerrarVentana()
    {
        stageActual.close();
    }

    

}