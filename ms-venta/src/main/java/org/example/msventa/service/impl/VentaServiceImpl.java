package org.example.msventa.service.impl;

import org.example.msventa.entity.Venta;
import org.example.msventa.entity.VentaDetalle;
import org.example.msventa.feign.ClienteFeign;
import org.example.msventa.feign.ProductoFeign;
import org.example.msventa.dato.Cliente;
import org.example.msventa.dato.Producto;
import org.example.msventa.repository.VentaRepository;
import org.example.msventa.service.VentaService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired private VentaRepository ventaRepository;
    @Autowired private ClienteFeign clienteFeign;
    @Autowired private ProductoFeign productoFeign;
//    private static final Logger log = LoggerFactory.getLogger(VentaServiceImpl.class);

    @Override
    public List<Venta> listar() {
        List<Venta> ventas = ventaRepository.findAll();
        for (Venta venta : ventas) {
            Cliente cliente = clienteFeign.obtenerPorId(venta.getClienteId()).getBody();
            venta.setCliente(cliente);
            for (VentaDetalle detalle : venta.getDetalles()) {
                Producto producto = productoFeign.obtenerPorId(detalle.getProductoId()).getBody();
                detalle.setProducto(producto);
            }
        }
        return ventas;
    }

    @Override
    public Optional<Venta> obtener(Integer id) {
        Optional<Venta> venta = ventaRepository.findById(id);
        venta.ifPresent(v -> {
            Cliente cliente = clienteFeign.obtenerPorId(v.getClienteId()).getBody();
            v.setCliente(cliente);
            for (VentaDetalle d : v.getDetalles()) {
                Producto producto = productoFeign.obtenerPorId(d.getProductoId()).getBody();
                d.setProducto(producto);
            }
        });
        return venta;
    }

    @Override
    public List<Venta> pendientes(Integer clienteId) {
        return ventaRepository.findByClienteIdAndEstado(clienteId, "SIN_PAGAR");
    }


    @Override
    public List<Venta> pagadas(Integer clienteId) {
        List<Venta> ventas = ventaRepository.findByClienteIdAndEstado(clienteId, "PAGADA");
        for (Venta venta : ventas) {
            for (VentaDetalle detalle : venta.getDetalles()) {
                Producto productoDto = productoFeign.obtenerPorId(detalle.getProductoId()).getBody();
                detalle.setProducto(productoDto);
            }
        }
        return ventas;
    }
    @Override
    public List<Venta> pagadasConEstadosLicencia(Integer clienteId, List<String> estadosLicencia) {
        List<Venta> ventas;

        if (clienteId != null) {
            ventas = ventaRepository.findByClienteIdAndEstadoAndEstadoLicenciaIn(clienteId, "PAGADA", estadosLicencia);
        } else {
            ventas = ventaRepository.findByEstadoAndEstadoLicenciaIn("PAGADA", estadosLicencia);
        }

        for (Venta venta : ventas) {
            for (VentaDetalle detalle : venta.getDetalles()) {
                Producto productoDto = productoFeign.obtenerPorId(detalle.getProductoId()).getBody();
                detalle.setProducto(productoDto);
            }
        }

        return ventas;
    }



    @Override
    public List<Venta> obtenerByCliente(Integer id) {
//        List<Venta> venta = new ArrayList<>();
        List<Venta> ventas = ventaRepository.getByClienteId(id);
        if (ventas.isEmpty()) {
            throw new DataIntegrityViolationException("No se encontraron ventas con ese el id "+id +" del cliente");
        }
        for (Venta venta : ventas) {
            for (VentaDetalle detalle : venta.getDetalles()) {
                Producto productoDto = productoFeign.obtenerPorId(detalle.getProductoId()).getBody();
                detalle.setProducto(productoDto);
            }
        }
        return ventas;
    }

    @Override
    public void marcarPagada(Integer id) {
        Venta v = ventaRepository.findByIdAndEstado(id, "SIN_PAGAR")
                .orElseThrow(() -> new RuntimeException("Venta no encontrada o ya pagada"));
        v.setEstado("PAGADA");
        ventaRepository.save(v);

        try {
            clienteFeign.actualizarEstado(v.getClienteId().longValue());
        } catch (Exception e) {
            // Manejo de excepciones (registro, alertas, etc.)
//            log.error("Error al actualizar el estado del cliente: ", e);
        }

    }

    @Override
    public Venta registrar(Venta venta) {

        // 1. validar cliente
        Cliente cliente = clienteFeign.obtenerPorId(venta.getClienteId()).getBody();
        if (cliente == null || !Boolean.TRUE.equals(cliente.getEstado())) {
            throw new RuntimeException("Cliente no válido o inactivo");
        }

        // 2. origen por defecto
        if (venta.getOrigen() == null || venta.getOrigen().isBlank()) {
            venta.setOrigen("CLIENTE");
        }

        // 3. si la venta la crea un trabajador → debe traer trabajadorId
        if ("TRABAJADOR".equalsIgnoreCase(venta.getOrigen()) && venta.getTrabajadorId() == null) {
            throw new RuntimeException("Se requiere trabajadorId para ventas de origen TRABAJADOR");
        }
        if (venta.getEstadoLicencia() == null) {
            venta.setEstadoLicencia("NO_APLICA");
        }

        // 4. estado inicial → SIN_PAGAR
        venta.setEstado("SIN_PAGAR");
        venta.setFechaVenta(LocalDate.now());

        double total = 0.0;
        for (VentaDetalle detalle : venta.getDetalles()) {
            Producto producto = productoFeign.obtenerPorId(detalle.getProductoId()).getBody();
            if (producto == null || producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Producto sin stock: ID " + detalle.getProductoId());
            }

            detalle.setPrecioUnitario(producto.getPrecioUnitario());
            detalle.setSubtotal(producto.getPrecioUnitario() * detalle.getCantidad());
            total += detalle.getSubtotal();

            detalle.setVenta(venta); // vinculación
        }

        venta.setTotal(total);
        Venta ventaGuardada = ventaRepository.save(venta);

        // 🔁 enriquecer con cliente y productos
        ventaGuardada.setCliente(cliente);
        for (VentaDetalle d : ventaGuardada.getDetalles()) {
            Producto producto = productoFeign.obtenerPorId(d.getProductoId()).getBody();
            d.setProducto(producto);
        }

        return ventaGuardada;
    }

    @Override
    public void eliminar(Integer id) {
        ventaRepository.deleteById(id);
    }
}
