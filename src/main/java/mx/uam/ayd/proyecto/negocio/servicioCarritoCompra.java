package mx.uam.ayd.proyecto.negocio;

import java.util.List;

import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.repositoriocarritoCompra;
import mx.uam.ayd.proyecto.conffigPD.gestionCliente;
import mx.uam.ayd.proyecto.datos.repositorioCliente;
import mx.uam.ayd.proyecto.datos.repositorioIntermedioCarrito;
import mx.uam.ayd.proyecto.datos.repositorioProducto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Cliente;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.IntermediaCarritoProd;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.Producto;
import mx.uam.ayd.proyecto.negocio.EntidadNegocio.carritoCompra;

@Service
public class servicioCarritoCompra {

    private final repositoriocarritoCompra repoCarrito;
    private final repositorioCliente repoCliente;
    private final repositorioProducto repositorioProd;
    private final repositorioIntermedioCarrito repoInter;
    private int sobrantes;

    // Añadimos ambos repositorios al constructor
    public servicioCarritoCompra(repositoriocarritoCompra repoCarrito, repositorioCliente repoCliente,
            repositorioProducto rp, repositorioIntermedioCarrito repoInter) {
        this.repoCarrito = repoCarrito;
        this.repoCliente = repoCliente;
        this.repositorioProd = rp;
        this.repoInter = repoInter;
    }

    /*
     * public boolean agregarItem(Producto producto, int cantidad) {
     * // verificamos si el cliente cuenta con un carrito de compra
     * // recuperamos al cliente con el Id, va a carrito ----
     * // carritoCompra----idCarrito
     * Cliente dueño =
     * repoCliente.findByIdCliente(gestionCliente.getInstance().getIdActivo());
     * sobrantes = 0;
     * 
     * if (producto.getcantidadStock() <= cantidad) {
     * return false;
     * }
     * 
     * if (dueño.getCarritoCompra() == null) {
     * System.out.println("No tiene carrito, creando carrito");
     * carritoCompra nuevo = new carritoCompra();
     * // agreamos producto dentro del carrito
     * nuevo.setProducto(producto);
     * // agregamos la cantidad total de unidades del mismmo producto
     * nuevo.setCantidadTotalCompra(cantidad);
     * // agregamos precio total
     * float total = producto.getPrecio() * cantidad;
     * nuevo.setTotalCalculado(total);
     * 
     * dueño.setcarritoCompra(nuevo);
     * // decrementamos los productos en existencia
     * sobrantes = producto.getcantidadStock() - cantidad;
     * producto.setcantidadStock(sobrantes);
     * // agregamos las unidades que el clietne quiere del producto
     * repoCarrito.save(nuevo); // dara error si no lo ponemos ya que el id del
     * carrito dentro de usuario es
     * // unicamente una refererncia y apuntaria a null
     * // guardamos al dueño con la referencia del carrito
     * repoCliente.save(dueño);
     * // actualizamos la cantidad dentro de stock;
     * repositorioProd.save(producto);
     * return true;
     * }
     * 
     * 
     * System.out.println("Ya tiene carrito, guardando");
     * carritoCompra posee = dueño.getCarritoCompra(); // recuperamos el carrito que
     * ya tiene el dueño
     * 
     * if(!posee.setProducto(producto)){
     * return false;
     * }
     * posee.setCantidadTotalCompra(cantidad);
     * // calculamos total de compra
     * float total = producto.getPrecio() * cantidad;
     * posee.setTotalCalculado(total);
     * dueño.setcarritoCompra(posee);
     * // decrementamos los productos en existencia
     * sobrantes = producto.getcantidadStock() - cantidad;
     * producto.setcantidadStock(sobrantes);
     * // agregamos las unidades que el clietne quiere del producto
     * repositorioProd.save(producto);
     * // guardamos el carrito y el el cliente con se carrito actualizado
     * repoCarrito.save(posee);
     * repoCliente.save(dueño);
     * 
     * return true;
     * 
     * }
     */

    public boolean agregarItem(Producto producto, int cantidad) {

        Cliente dueño = repoCliente.findByIdCliente(gestionCliente.getInstance().getIdActivo());
        IntermediaCarritoProd inter = new IntermediaCarritoProd();
        sobrantes = 0;

        if (producto.getcantidadStock() <= cantidad) {
            return false;
        }

        if (dueño.getCarritoCompra() == null) {
            System.out.println("No tiene carrito, creando carrito");
            carritoCompra nuevo = new carritoCompra();
            // agregamos producto a carrito
            nuevo.setProducto(producto);

            // agregamos a la tabla intermedia el producto
            inter.setCarrito(nuevo);
            inter.setCantidadTotalProd(cantidad);
            inter.setProd(producto);

            // agregamos el TOTAL de compras sumando todos los productos
            nuevo.setCantidadTotalCompra(cantidad);
            // agregamos precio total
            float total = producto.getPrecio() * cantidad;
            nuevo.setTotalCalculado(total);

            // le asignamos al dueño su carrito activo
            dueño.setcarritoCompra(nuevo);
            // decrementamos las unidades del producto
            sobrantes = producto.getcantidadStock() - cantidad;
            producto.setcantidadStock(sobrantes);
            repoCarrito.save(nuevo);
            repoCliente.save(dueño);
            repositorioProd.save(producto);
            repoInter.save(inter);
            return true;
        }

        System.out.println("Ya tiene carrito, guardando");
        carritoCompra posee = dueño.getCarritoCompra(); // recuperamos el carrito que ya tiene el dueño

        if (!posee.setProducto(producto)) {
            return false;
        }

        // calculamos las unidades totales de compra
        posee.setCantidadTotalCompra(cantidad);
        // calculamos total de compra
        float total = producto.getPrecio() * cantidad;
        posee.setTotalCalculado(total);
        dueño.setcarritoCompra(posee);
        // decrementamos los productos en existencia
        sobrantes = producto.getcantidadStock() - cantidad;
        producto.setcantidadStock(sobrantes);

        // ingresamos toda la informacion a la tabla intermedia
        inter.setCarrito(posee);
        inter.setProd(producto);
        inter.setCantidadTotalProd(cantidad);

        // agregamos las unidades que el clietne quiere del producto
        repositorioProd.save(producto);
        repoCarrito.save(posee);
        repoCliente.save(dueño);
        repoInter.save(inter);
        return true;
    }

    // A partir de aqui comienza la HU02
    public carritoCompra recuperaProductoEnCarrito() {
        // recuperamos el cliente
        Cliente clienteActivo = repoCliente.findByIdCliente(gestionCliente.getInstance().getIdActivo());
        // recuperamos el carrito
        carritoCompra car = clienteActivo.getCarritoCompra();
        if (car != null) {
            /*
            if (car.getTotalCalculado() > 300) {
                car.setenvioGratis(true);
            } else {
                car.setenvioGratis(false);
        }
        */
       car.validarEnvioGratis();
       System.out.println("El envio gratis es: " + car.getenvioGratis());
            return car;
        }
        return null;
    }

    // Eliminar producto de carrito
    public boolean EliminarProdCarrito(Producto prod) {

        Cliente clienteActivo = repoCliente.findByIdCliente(gestionCliente.getInstance().getIdActivo());
        IntermediaCarritoProd relacionEncontrada = null;
        carritoCompra car = clienteActivo.getCarritoCompra();
        // recuperamos la tabla intermedia con el id del carrito del dueño con sesion
        // activa
        List<IntermediaCarritoProd> relaciones = repoInter.findByCarIdCarrito(car.getid());
        int cantidadxUnidad = 0;
        for (IntermediaCarritoProd inter : relaciones) {
            if (inter.getProd() != null && inter.getProd().equals(prod)) {
                cantidadxUnidad = inter.getCantidadTotalProd();
                relacionEncontrada = inter;
                break;
            }
        }

        // si no existe relacion se retorna null
        if (relacionEncontrada == null) {
            return false;
        }

        // 3. Eliminar la relación de la tabla intermedia
        repoInter.delete(relacionEncontrada);

        // Quitamos el producto de la tabla
        if (car.removerProducto(prod, cantidadxUnidad)) {

            repoCarrito.save(car);
            return true;
        }

        return false;
    }

    public Cliente recuperaClienToDireccion() {
        Cliente dueño = repoCliente.findByIdCliente(gestionCliente.getInstance().getIdActivo());
        return dueño;
    }
    
    public carritoCompra recuperaCarritoPorCliente(long idCliente) {
        Cliente cliente = repoCliente.findByIdCliente(idCliente);
        if (cliente == null) {
            return null;
        }
        return cliente.getCarritoCompra();
    }
    
    /**
     * Recupera el total calculado del carrito asociado al cliente indicado.
     *
     * @param idCliente identificador del cliente cuyo carrito se desea consultar.
     * @return total calculado del carrito, o 0.0 si el cliente no tiene carrito.
     */
    public double recuperaTotalCalculadoPorCliente(long idCliente) {
        carritoCompra carrito = recuperaCarritoPorCliente(idCliente);
        if (carrito == null) {
            return 0.0;
        }
        return carrito.getTotalCalculado();
    }

    public boolean simulacionConexionApi() {
        return true;
    }
}