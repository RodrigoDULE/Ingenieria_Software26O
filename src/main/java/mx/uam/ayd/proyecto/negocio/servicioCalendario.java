package mx.uam.ayd.proyecto.negocio;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//leo D
import org.springframework.stereotype.Service;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Evento;
import mx.uam.ayd.proyecto.datos.RepositorioEvento;
//import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Empleado;
//import mx.uam.ayd.proyecto.datos.RepositorioEmpleado;




@Service
public class servicioCalendario {

    //constructor 
    private RepositorioEvento repoEvento;
    public servicioCalendario(RepositorioEvento repoEvento){
        this.repoEvento = repoEvento;
    }



    //(opcional pero por si se ocupa)
    public Long reucuperaIdEvento(long idEvento){
        return repoEvento.findByIdEvento(idEvento).getIdEvento();
    }

    //metodo para recuperar eventos por fecha (del evento)
    
    public List<Evento> recuperaEventoporFecha(LocalDate fechaE){
        return (List<Evento>) repoEvento.findByFechaE(fechaE);
    }
    
    
    //recuperar eventos por notificacion
    public Evento recuperaPorNotificacion(LocalDate notificacion){
        return repoEvento.findByNotificacion(notificacion);
    }

    
    //calcular repeticion de notificiaciones (semanas)
    
    public List<LocalDate> calcularRepeticionSemanas(LocalDate fechaE, LocalDate notificacion, int numsemanas){
        List<LocalDate> notssemanas = new ArrayList<>();
        notssemanas.add(notificacion);
        int j=numsemanas;
        if (numsemanas > 0){
            while(fechaE.isAfter(notificacion.plusWeeks(j))==true){

                notssemanas.add(notificacion.plusWeeks(j));
                j=j+numsemanas;
            }
        }else{

            throw new IllegalArgumentException();
    
        }

        return notssemanas;
    }




    //calcular repeticion de notificaciones (dias)


    public List<LocalDate> calcularRepeticionDias(LocalDate fechaE, LocalDate notificacion, int numdias){
        List <LocalDate> notsdias = new ArrayList<>();
        notsdias.add(notificacion);
        int i= numdias;

        //if valida que los dias no sean negativos ni 0
        if(numdias>0){
            while (fechaE.isAfter(notificacion.plusDays(i))==true){//itera hasta que la fecha de notificacion sea mayor a la del evento
                
            notsdias.add(notificacion.plusDays(i));
                  i=i+ numdias;

            }
        }else{
            throw new IllegalArgumentException();
            //regresa una excepcion 
        }
        
        return notsdias;
    } 


    //administrar notificaciones de eventos
    public Evento administrarNotificaciones(Evento evento, int numdias, int numsemanas,LocalDate nuevaNotificacion){
        
        evento.setNotificacion(nuevaNotificacion);
        
        LocalDate fechaE = evento.getFechaE();
        LocalDate notificacion = evento.getNotificacion();
        
        if(notificacion.isAfter(fechaE)==true){
            throw new IllegalArgumentException();
        }
        /*if(numdias==0 ){


        }*/
        if(numdias>0){
          calcularRepeticionDias(fechaE, notificacion, numdias);  

        }
        if(numsemanas>0){
            calcularRepeticionSemanas(fechaE, notificacion, numsemanas);
        }
        evento.setNotificacion(notificacion);
        evento = repoEvento.save(evento);
        

        return evento;
    }

}

