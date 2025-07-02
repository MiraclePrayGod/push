package com.example.mspago.service.impl;
import com.example.mspago.dto.*;
import com.example.mspago.entity.Pago;
import com.example.mspago.feign.ProductoFeign;
import com.example.mspago.feign.VentaFeign;
import com.example.mspago.repository.PagoRepository;
import com.example.mspago.service.PagoService;
import com.example.mspago.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagoServiceImpl implements PagoService {

    @Autowired private PagoRepository pagoRepository;
    @Autowired private VentaFeign ventaFeign;
    @Autowired private ProductoFeign productoFeign;
    @Autowired private StorageService storageService;
    @Override
    public List<VentaDTO> ventasPendientes(Long clienteId) {
        return ventaFeign.listarPendientes(clienteId.intValue()).getBody();
    }
    @Override
    public String obtenerNombreComprobantePorPagoId(Long pagoId) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + pagoId));
        String url = pago.getComprobanteUrl();
        if (url == null || url.isEmpty()) {
            throw new RuntimeException("El pago no tiene comprobante");
        }

        // Extraer solo el nombre del archivo, asumiendo que la URL es tipo "/comprobantes/archivo.ext"
        // o puede ser el nombre directamente
        String nombreArchivo = url.contains("/") ? url.substring(url.lastIndexOf("/") + 1) : url;
        return nombreArchivo;
    }

    @Override
    public Pago registrar(PagoRequest r, MultipartFile archivo) {

        /* 1. Obtener y validar la venta */
        VentaDTO venta = ventaFeign.obtener(r.getVentaId()).getBody();
        if (venta == null || !"SIN_PAGAR".equals(venta.getEstado())) {
            throw new RuntimeException("Venta no encontrada o ya pagada");
        }

        /* 2. Validar método de pago (ejemplo simple) */
        if ("TRABAJADOR".equals(venta.getOrigen()) &&
                !"CONTADO".equals(r.getMetodo()) && !"TRANSFERENCIA".equals(r.getMetodo())) {
            throw new RuntimeException("Método no permitido para ventas presenciales");
        }

        /* 3. Guardar comprobante si es transferencia */
        String url = null;
        if ("TRANSFERENCIA".equals(r.getMetodo())) {
            if (archivo == null) throw new RuntimeException("Debe adjuntar comprobante");
            url = storageService.store(archivo);
        }

        /* 4. Registrar pago */
        Pago pago = new Pago();
        pago.setVentaId(venta.getId());
        pago.setClienteId(venta.getClienteId());
        pago.setTrabajadorId(r.getTrabajadorId());
        pago.setMetodo(r.getMetodo());
        pago.setMonto(venta.getTotal());
        pago.setFechaPago(LocalDateTime.now());
        pago.setComprobanteUrl(url);
        pagoRepository.save(pago);
        if ("TRANSFERENCIA".equalsIgnoreCase(pago.getMetodo())) {
            ventaFeign.actualizarEstadoLicencia(venta.getId(), "PENDIENTE");
        }
        /* 5. Descontar stock de cada producto */
        for (DetalleVentaDTO d : venta.getDetalles()) {
            ProductoDTO prod = productoFeign.obtenerPorId(d.getProductoId()).getBody();
            if (prod.getStock() < d.getCantidad()) {
                throw new RuntimeException("Sin stock para producto ID: " + d.getProductoId());
            }
            prod.setStock(prod.getStock() - d.getCantidad());
            productoFeign.actualizar(prod.getId(), prod);
        }

        /* 6. Marcar venta como pagada */
        ventaFeign.marcarPagada(venta.getId());

        return pago;
    }

    @Override
    public Pago obtener(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
    }


}