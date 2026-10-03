package mx.uam.ayd.proyecto.presentacion.HU_06;

import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.DetalleOrden;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Label; 
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;


@Component
public class VistaDetalleDeOrden {


    @FXML private TableView<DetalleOrden> tablaProductos;
    @FXML private TableColumn<DetalleOrden, String> colProducto;
    @FXML private TableColumn<DetalleOrden, String> colSKU;
    @FXML private TableColumn<DetalleOrden, Integer> colCantidad;
    @FXML private TableColumn<DetalleOrden, Double> colPrecio;
    @FXML private TableColumn<DetalleOrden, Double> colTotal;

    @FXML private Label lblTotal;
    @FXML private Label lblFechaCreacion;
    @FXML private Label lblOrdenId;
    @FXML private Label lblNombreProveedor;

    private ControladorDetalleOrden controlador;
    private Stage stageActual;

    private OrdenDeCompra orden;
    private String proveedor;

    public VistaDetalleDeOrden() {}

    public void setControlador(ControladorDetalleOrden controlador)
    {
        this.controlador = controlador;
    }

    public void configurarTabla(List<DetalleOrden> listaDetalles) {
        
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("subTotalLote"));

        // Entramos al Detalle -> sacamos el Insumo -> sacamos el Nombre
        colProducto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInsumo().getNombre()));
        colSKU.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInsumo().getUnidadDeMedida()));
        colPrecio.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getInsumo().getPrecio()));

        // Finalmente, le inyectamos la lista completa a la tabla
        //tablaProductos.getItems().clear(); // Limpiamos por si había datos viejos
        tablaProductos.getItems().setAll(listaDetalles);
    }


    public void muestraDetalle(Stage ventanaActual, OrdenDeCompra orden, String nombreProveedor)
    {
        if(!Platform.isFxApplicationThread())
        {
            Platform.runLater(() -> muestraDetalle(ventanaActual, orden, nombreProveedor));
            return;
        }

        this.stageActual = ventanaActual;
        this.orden = orden;
        this.proveedor = nombreProveedor;

        try{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana_DetallesDeOrden.fxml"));
            loader.setController(this);

            Scene nuevaEscena = new Scene(loader.load());
            stageActual.setScene(nuevaEscena);
            stageActual.setTitle("Autorizar Orden");

            
            

            lblTotal.setText("$" + orden.getTotal() + " MXN");
            lblOrdenId.setText("PO-2026-" + orden.getId());
            lblFechaCreacion.setText(String.valueOf(orden.getFechaCreacion()));
            lblNombreProveedor.setText("  " + nombreProveedor);

            List<DetalleOrden> listaDeEstaOrden = orden.getDetalles();
            configurarTabla(listaDeEstaOrden);

            stageActual.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void cancelar()
    {
        if(controlador != null)
        {
            controlador.volverAOrdenesPendientes(stageActual);
        }
    }

    @FXML
    public void confirmar()
    {
        if(controlador != null){
            controlador.irAConfirmarAutorizacion(stageActual, orden, proveedor);
        }
    }

}