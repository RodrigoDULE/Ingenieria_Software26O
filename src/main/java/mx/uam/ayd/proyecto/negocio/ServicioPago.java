package mx.uam.ayd.proyecto.negocio;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioDetallesCompra;
import mx.uam.ayd.proyecto.datos.repositorioCliente;
import mx.uam.ayd.proyecto.datos.repositorioCompra;
import mx.uam.ayd.proyecto.datos.repositorioProducto;
import mx.uam.ayd.proyecto.datos.repositoriocarritoCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Compra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.DetallesCompra;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.carritoCompra;
import mx.uam.ayd.proyecto.conffigPD.gestionCliente;

@Service
public class ServicioPago {
    private repositorioCliente repositorioCliente;
    private repositorioCompra repositorioCompra;
    private RepositorioDetallesCompra repositorioDetallesCompra;
    private repositoriocarritoCompra repositoriocarritoCompra;
    private repositorioProducto repositorioProducto;


    ServicioPago() {
    }

    @Autowired
    ServicioPago(repositorioCliente repositorioCliente, repositorioCompra repositorioCompra,
            RepositorioDetallesCompra repositorioDetallesCompra, repositoriocarritoCompra repositoriocarritoCompra, repositorioProducto repositorioProducto) {
        this.repositorioCliente = repositorioCliente;
        this.repositorioCompra = repositorioCompra;
        this.repositorioDetallesCompra = repositorioDetallesCompra;
        this.repositoriocarritoCompra = repositoriocarritoCompra;
        this.repositorioProducto = repositorioProducto;
    }

    public carritoCompra recuperaCarritoCompra(long idUsuario) {
        Cliente user = repositorioCliente.findByIdCliente(idUsuario);
        return user.getCarritoCompra();
    }

    public boolean peticionPagarCarrito(carritoCompra carCompra) {
        // se detiene cuando un elemento dentro de la lista no cumple la condicion del
        // predicado, y retorna un false
        boolean stockValido = carCompra.getProductoenCarrito().stream().allMatch(prod -> prod.getcantidadStock() >= 1);

        if (!stockValido) {
            return false;
        }

        if(carCompra.getProductos().isEmpty()){
            System.out.println("El carrito esta vacio");
            return false;
        }

        System.out.println("El stock de todos los productos es valido, comenzando la peticion de compra");
        // recuperamos al cliente mediante su id de usuario
        long idCliente = gestionCliente.getInstance().getIdActivo();
        Cliente user = repositorioCliente.findByIdCliente(idCliente);

        if(user == null){
            return false;
        }

        LocalDate ha = LocalDate.now();
        // vinculamos la compra con el cliente
        Compra nueva = new Compra();

        nueva.setClienteFK(user);
        nueva.setCarrito(carCompra);
        nueva.setFecha(ha);
        nueva.setMonto(carCompra.getTotalCalculado());
        nueva.setTotalProductos(carCompra.getCantidadTotalCompra());
        carCompra.setCompra(nueva);
        // creamos el detallle de la compra
        repositorioCompra.save(nueva);
        
        carCompra.getInterCar().forEach(prodCompra -> {
            DetallesCompra detCompra = new DetallesCompra();
            System.out.println("Procesando el producto: " + prodCompra.getProd().getnombre() + ", " + prodCompra.getProd().getidProducto());
            //actualizamos el stock del producto que ya fue comprado
            prodCompra.getProd().setcantidadStock(prodCompra.getProd().getcantidadStock()-prodCompra.getCantidadTotalProd());

            detCompra.setCompraFK(nueva);
            detCompra.setProductoFK(prodCompra.getProd());
            detCompra.setPrecioProducto(prodCompra.getProd().getPrecio());
            detCompra.setCantidadComprada(prodCompra.getCantidadTotalProd());
            
            nueva.setDetalleCompra(detCompra);//guarda la referencia para despues recuperar los detalles desde la compra
            
            repositorioDetallesCompra.save(detCompra);
            repositorioProducto.save(prodCompra.getProd()); //para que se guarde e producto con el stock actualzaido
        });

        //removemos y dejamos el carrito limpio, sin ningun producto dentro de el
        if(carCompra.vaciarCarrito()){
            carCompra.getInterCar().clear();
        }

        repositoriocarritoCompra.save(carCompra);


        return true;
    }
}
