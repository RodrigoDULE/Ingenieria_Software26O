package mx.uam.ayd.proyecto.conffigPD;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class GestionEscena {
    
    //cramos la instancia
    private static GestionEscena intanEscena;

    private Stage stage;
    private Scene inicial;

    private GestionEscena(){}

    //modelo para obtener la instancia unica
    public static GestionEscena getInstEscena(){
        if(intanEscena == null){
            intanEscena = new GestionEscena();
        }

        return intanEscena;
    }

    public void iniciarEscena(Stage stage){
        this.stage = stage;
    }

    public void iniciarCatalogo(Scene stage){
        this.inicial = stage;
    }

    //nos ayudara a volver a la ventana anterior
    public Stage getStage(){
        return this.stage;
    }

    //nos ayudara a volver a la ventana inicial del catalogo
    public Scene getInicial(){
        return this.inicial;
    }
}
