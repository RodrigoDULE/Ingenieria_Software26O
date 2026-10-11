package mx.uam.ayd.proyecto.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.repositorioCliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.carritoCompra;

@Service 
public class ServicioPago {
    private repositorioCliente repositorioCliente;

    ServicioPago(){}

    @Autowired 
    ServicioPago(repositorioCliente repositorioCliente){
        this.repositorioCliente = repositorioCliente;
    }

    public carritoCompra recuperaCarritoCompra(long idUsuario){
        Cliente user = repositorioCliente.findByIdCliente(idUsuario);
        return user.getCarritoCompra();
    }
}
