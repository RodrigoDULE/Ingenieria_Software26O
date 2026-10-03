package mx.uam.ayd.proyecto.presentacion.HU10GuardarContenido;


import java.io.IOException;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import mx.uam.ayd.proyecto.negocio.EntidadNegocio.FormularioMarketing;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.PublicacionMarketing;


@Component
public class VistaEditarMarketing {


    private Stage escenario;


    private FormularioMarketing formularioActual;


    private ControladorGuardarContenido controlador;



    @FXML
    private TextField nameField;

    @FXML
    private TextField dateField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField platformField;



    public void setControlador(ControladorGuardarContenido controlador){

        this.controlador = controlador;

    }



    public void muestra(FormularioMarketing formulario){


        this.formularioActual = formulario;


        try {

            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/ventana-editar-contenidoG.fxml")
            );


            loader.setController(this);


            Parent root = loader.load();


            escenario = new Stage();

            escenario.setTitle("Editar Contenido");

            escenario.setScene(
                new Scene(root)
            );


            cargarDatos();


            escenario.show();


        } catch(IOException e){

            e.printStackTrace();

        }

    }




    private void cargarDatos(){


        nameField.setText(
            formularioActual.getNombre()
        );


        dateField.setText(
            formularioActual
            .getFechaEstimadaPublicacion()
            .toString()
        );


        typeField.setText(
            formularioActual
            .getTipoContenido()
            .toString()
        );


        platformField.setText(
            formularioActual
            .getPlataformasDestino()
            .toString()
        );


    }



    @FXML
    public void handleSave(){


        formularioActual.setNombre(
            nameField.getText()
        );


        controlador.actualizarFormulario(
            formularioActual
        );


    }



    @FXML
    public void handlePublish(){


        PublicacionMarketing publicacion =
                new PublicacionMarketing();


        publicacion.setEstado(true);

        publicacion.setFechaProgramada(
                LocalDate.now()
        );


        controlador.publicarFormulario(
            formularioActual.getId(),
            publicacion
        );


    }


}