package mx.uam.ayd.proyecto.presentacion.HU_07;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.OrdenDeCompra;

@Component
public class ControladorLoteCerradoExito {

    private final VistaLoteCerradoExito vistaExito;

    @Autowired
    public ControladorLoteCerradoExito(VistaLoteCerradoExito vistaExito)
    {
        this.vistaExito = vistaExito;
    }

    @PostConstruct
    private void inicializarControlador()
    {
        vistaExito.setControlador(this);
    }

    public void iniciaVentanaLoteCerradoExito(OrdenDeCompra orden)
    {
        vistaExito.muestraLoteCerrado(orden);
    }

}