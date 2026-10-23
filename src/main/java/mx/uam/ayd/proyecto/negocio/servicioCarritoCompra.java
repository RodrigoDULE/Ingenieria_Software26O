package mx.uam.ayd.proyecto.negocio;


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

    // Añadimos ambos repositorios al constructor
    public servicioCarritoCompra(repositoriocarritoCompra repoCarrito, repositorioCliente repoCliente,
            repositorioProducto rp, repositorioIntermedioCarrito repoInter) {
        this.repoCarrito = repoCarrito;
        this.repoCliente = repoCliente;
        this.repositorioProd = rp;
        this.repoInter = repoInter;
    }

    public boolean agregarItem(Producto producto, int cantidad) {

        Cliente dueño = repoCliente.findByIdCliente(gestionCliente.getInstance().getIdActivo());
        IntermediaCarritoProd inter = new IntermediaCarritoProd();

        if (producto.getcantidadStock() < cantidad || producto.getcantidadStock() == 0) {
            return false;
        }

        // si el dueño no tiene carrito o si su carrito ha sido pagado anteriormente, se
        // le asigna uno nuevo
        if (dueño.getCarritoCompra() == null || dueño.getCarritoCompra().getEstado() == true) {

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
            /*
             * LAs cantidades se descuentan cuando el cliente realiza la compra, en otro
             * caso, el stock del producto se quedaria vacio demasiado pronto
             * sobrantes = producto.getcantidadStock() - cantidad;
             * producto.setcantidadStock(sobrantes);
             */
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
        // sobrantes = producto.getcantidadStock() - cantidad;
        // producto.setcantidadStock(sobrantes);

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
             * if (car.getTotalCalculado() > 300) {
             * car.setenvioGratis(true);
             * } else {
             * car.setenvioGratis(false);
             * }
             */
            car.validarEnvioGratis();
            System.out.println("El envio gratis es: " + car.getenvioGratis());
            return car;
        }
        return null;
    }

    public boolean EliminarProdCarrito(Producto producto) {
        // 1. Obtener cliente y carrito
        Cliente clienteActivo = repoCliente.findByIdCliente(gestionCliente.getInstance().getIdActivo());
        carritoCompra car = clienteActivo.getCarritoCompra();

        // 2. Buscar el elemento dentro de la lista intermedia
        IntermediaCarritoProd itemABorrar = car.getInterCar().stream()
                .filter(inter -> inter.getProd().getidProducto() == producto.getidProducto()) // O usando .equals()
                .findFirst()
                .orElse(null);

        // 3. Si no está en el carrito, retornamos false
        if (itemABorrar == null) {
            System.out.println("El producto no está dentro del carrito");
            return false;
        }

        System.out.println("El producto está dentro. Procediendo a eliminar...");

        // 4. AL REMOVERLO DE LA LISTA: orphanRemoval = true hace el DELETE en la BD
        // automáticamente
        if (car.removerProducto(producto, itemABorrar.getCantidadTotalProd())) {
            car.getInterCar().remove(itemABorrar);
            car.setCantidadTotalCompra(-1);
            // 5. Guardamos el estado actualizado del carrito (o cliente)
            repoCliente.save(clienteActivo);
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