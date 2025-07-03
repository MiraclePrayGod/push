package org.example.msfacturacion.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.msfacturacion.config.EmisorProperties;
import org.example.msfacturacion.dato.*;
import org.example.msfacturacion.entity.Factura;
import org.example.msfacturacion.entity.FacturaDetalle;
import org.example.msfacturacion.feign.ClienteFeign;
import org.example.msfacturacion.feign.ProductoFeign;
import org.example.msfacturacion.feign.VentaFeign;
import org.example.msfacturacion.repository.FacturaRepository;
import org.example.msfacturacion.service.FacturaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacturaServiceImpl implements FacturaService {
    private final VentaFeign ventaFeign;
    private final ClienteFeign clienteFeign;
    private final FacturaRepository facturaRepo;
    private final EmisorProperties emisor;

    private static final AtomicLong SEQ = new AtomicLong(1);

    // Método 1: listar ventas pagadas por cliente
    @Override
    public List<VentaDTO> ventasPagadas(Long clienteId) {
        return ventaFeign.pagadas(clienteId).getBody();
    }

    // Método 2: emitir factura
    @Override
    @Transactional
    public FacturaDTO emitir(FacturaRequest req) {
        List<VentaDTO> ventas = req.getVentasIds().stream()
                .map(id -> ventaFeign.obtener(id).getBody())
                .filter(v -> "PAGADA".equalsIgnoreCase(v.getEstado()))
                .toList();

        if (ventas.isEmpty()) throw new RuntimeException("No hay ventas válidas");

        Long clienteId = ventas.get(0).getClienteId();
        boolean mismoCliente = ventas.stream()
                .allMatch(v -> v.getClienteId().equals(clienteId));
        if (!mismoCliente)
            throw new RuntimeException("Todas las ventas deben ser del mismo cliente");

        DatosClienteDTO cliente = clienteFeign.obtener(clienteId).getBody();

        Factura factura = new Factura();
        factura.setNumeroFactura(generarSerieCorrelativo());
        factura.setFechaEmision(LocalDate.now());
        factura.setMoneda(req.getMoneda());
        factura.setTipoComprobante("FACTURA");
        factura.setFormaPago(req.getFormaPago());
        factura.setMedioPago(req.getMedioPago());
        factura.setClienteId(clienteId);
        factura.setRucDniCliente(cliente.getNumeroDocumento());
        factura.setNombreCliente(cliente.getNombres() + " " + cliente.getApellidos());

        double subTotal = 0;
        List<FacturaDetalle> detalles = new ArrayList<>();

        for (VentaDTO v : ventas) {
            for (DetalleVentaDTO dv : v.getDetalles()) {
                ProductoDTO producto = productoFeign.obtener(dv.getProductoId());

                FacturaDetalle fd = new FacturaDetalle();
                fd.setVentaId(v.getId());
                fd.setProductoId(dv.getProductoId());
                fd.setNombreProducto(producto.getNombre());  // nombre del producto desde ms-producto
                fd.setDescripcion(producto.getDescripcion()); // descripción desde ms-producto
                fd.setCantidad(dv.getCantidad());
                fd.setUnidadMedida("UN");
                fd.setPrecioUnitario(dv.getPrecioUnitario());
                fd.setSubtotal(dv.getSubtotal());
                fd.setIgv(dv.getSubtotal() * 0.18);
                fd.setTotalLinea(dv.getSubtotal() * 1.18);
                fd.setFactura(factura);
                subTotal += dv.getSubtotal();

                detalles.add(fd);
            }
        }

        factura.setSubTotal(subTotal);
        factura.setTotalImpuestos(subTotal * 0.18);
        factura.setTotal(subTotal * 1.18);
        factura.setDetalles(detalles);

        facturaRepo.save(factura);
        ventas.forEach(v -> ventaFeign.facturar(v.getId()));

        return convertirADTO(factura);
    }

    // Método 3: listar facturas por cliente
    @Override
    public List<FacturaDTO> listarPorCliente(Long clienteId) {
        DatosClienteDTO cliente = clienteFeign.obtener(clienteId).getBody();
        if (cliente == null) {
            throw new RuntimeException("Cliente no encontrado");
        }
        return facturaRepo.findByClienteId(clienteId)
                .stream()
                .map(factura -> {
                    FacturaDTO facturaDTO = convertirADTO(factura); // Solo pasa la factura
                    facturaDTO.setCliente(cliente);  // Asigna el cliente manualmente
                    return facturaDTO;
                })
                .collect(Collectors.toList());
    }

    // Nuevo: listar todas las facturas
    @Override
    public List<FacturaDTO> listarFacturas() {
        List<Factura> facturas = facturaRepo.findAll();
        List<FacturaDTO> resultado = new ArrayList<>();
        for (Factura factura : facturas) {
            FacturaDTO facturaDTO = convertirADTO(factura);
            // Asignar datos completos del cliente
            if (factura.getClienteId() != null) {
                DatosClienteDTO cliente = clienteFeign.obtener(factura.getClienteId()).getBody();
                facturaDTO.setCliente(cliente);
            }
            // Asignar datos adicionales a cada detalle
            if (facturaDTO.getItems() != null && factura.getDetalles() != null) {
                List<FacturaDetalle> detallesEntity = factura.getDetalles();
                List<FacturaDetalleDTO> detallesDTO = facturaDTO.getItems();
                for (int i = 0; i < detallesDTO.size(); i++) {
                    FacturaDetalleDTO detalleDTO = detallesDTO.get(i);
                    FacturaDetalle detalleEntity = detallesEntity.get(i);
                    detalleDTO.setCantidad(detalleEntity.getCantidad());
                    detalleDTO.setDescripcion(detalleEntity.getDescripcion());
                    detalleDTO.setIgv(detalleEntity.getIgv());
                    detalleDTO.setNombreProducto(detalleEntity.getNombreProducto());
                    detalleDTO.setPrecioUnitario(detalleEntity.getPrecioUnitario());
                    detalleDTO.setSubtotal(detalleEntity.getSubtotal());
                    detalleDTO.setTotalLinea(detalleEntity.getTotalLinea());
                    detalleDTO.setVentaId(detalleEntity.getVentaId());
                    // Si necesitas más campos, agrégalos aquí
                }
            }
            resultado.add(facturaDTO);
        }
        return resultado;
    }

    // Nuevo: convertir entidad Factura a DTO (para PDF y controller)
    @Override
    public FacturaDTO convertirADTO(Factura f) {
        FacturaDTO dto = new FacturaDTO();
        dto.setId(f.getId());
        dto.setNumeroFactura(f.getNumeroFactura());
        dto.setFechaEmision(f.getFechaEmision());
        dto.setMoneda(f.getMoneda());
        dto.setTipoComprobante(f.getTipoComprobante());
        dto.setFormaPago(f.getFormaPago());
        dto.setMedioPago(f.getMedioPago());
        dto.setSubTotal(f.getSubTotal());
        dto.setTotalImpuestos(f.getTotalImpuestos());
        dto.setTotal(f.getTotal());

        List<FacturaDetalleDTO> detalleDTOs = new ArrayList<>();
        if (f.getDetalles() != null) {
            for (FacturaDetalle d : f.getDetalles()) {
                FacturaDetalleDTO dd = new FacturaDetalleDTO();
                dd.setNombreProducto(d.getNombreProducto());
                dd.setDescripcion(d.getDescripcion());
                dd.setCantidad(d.getCantidad());
                dd.setPrecioUnitario(d.getPrecioUnitario());
                dd.setSubtotal(d.getSubtotal());
                dd.setIgv(d.getIgv());
                dd.setTotalLinea(d.getTotalLinea());
                dd.setVentaId(d.getVentaId());
                // Si agregas más campos en FacturaDetalleDTO, asígnalos aquí
                detalleDTOs.add(dd);
            }
        }
        dto.setItems(detalleDTOs);

        // Asignar datos completos del cliente si están disponibles
        DatosClienteDTO cli = new DatosClienteDTO();
        cli.setId(f.getClienteId());
        cli.setNombres(f.getNombreCliente()); // Usar nombre completo si es empresa
        cli.setNumeroDocumento(f.getRucDniCliente());
        // Si tienes más campos de cliente en la entidad, asígnalos aquí
        dto.setCliente(cli);

        return dto;
    }

    private String generarSerieCorrelativo() {
        return "F001-" + String.format("%08d", SEQ.getAndIncrement());
    }
    private final ProductoFeign productoFeign;

}