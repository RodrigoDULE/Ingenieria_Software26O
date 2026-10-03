package mx.uam.ayd.proyecto.presentacion.HU10GuardarContenido;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import javafx.fxml.FXML;
import mx.uam.ayd.proyecto.negocio.ServicioGeneracionContenido.VariacionContenido;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.FormularioMarketing;

@Component
public class VistaGuardarContenido {


    private Stage escenario;


    @FXML
    private ListView<FormularioMarketing> listaContenidos;


    private ControladorGuardarContenido controlador;


    public void setControlador(ControladorGuardarContenido controlador){
        this.controlador = controlador;
    }



    public void muestra(){

        try {

            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/ventana-guardar-contenido.fxml")
            );

            loader.setController(this);

            Parent root = loader.load();

            escenario = new Stage();

            escenario.setTitle("Contenidos Guardados");
            escenario.setScene(new Scene(root));

            escenario.show();


        } catch (IOException e) {

            e.printStackTrace();

        }

    }



    public void mostrarContenido(FormularioMarketing formulario){

        System.out.println(formulario.getNombre());
        listaContenidos.getItems().clear();

        listaContenidos.getItems().add(formulario);

    }



    public void mostrarFormularios(List<FormularioMarketing> formularios){

        listaContenidos.getItems().clear();

        listaContenidos.getItems().addAll(formularios);

    }



    @FXML
    public void abrirContenido(){

        Object seleccionado =
                listaContenidos.getSelectionModel().getSelectedItem();


        if(seleccionado instanceof FormularioMarketing){

            escenario.close();

            // Aquí después abrirá la ventana de edición

            controlador.actualizarFormulario((FormularioMarketing) seleccionado);
        }

    }



    @FXML
    public void regresar(){

        escenario.close();

    }


}
