package org.example.msventa.service;

import org.example.msventa.dato.DetalleVentaConProductoDTO;
import org.example.msventa.entity.VentaDetalle;
import org.example.msventa.dato.Producto;
import org.example.msventa.feign.ProductoFeign;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

public class VentaDetalleMapper {

    private final ProductoFeign productoFeign;

    public VentaDetalleMapper(ProductoFeign productoFeign) {
        this.productoFeign = productoFeign;
    }

    public DetalleVentaConProductoDTO map(VentaDetalle detalle) {
        DetalleVentaConProductoDTO dto = new DetalleVentaConProductoDTO();
        dto.setProductoId(detalle.getProductoId());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());

        // Obtener info completa producto desde ms-producto via Feign
        ResponseEntity<Producto> resp = productoFeign.obtenerPorId(detalle.getProductoId());
        if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
            Producto p = resp.getBody();
            dto.setNombreProducto(p.getNombre());
            dto.setDescripcionProducto(p.getDescripcion());
        } else {
            dto.setNombreProducto("Producto no encontrado");
            dto.setDescripcionProducto("");
        }

        return dto;
    }

    public List<DetalleVentaConProductoDTO> mapList(List<VentaDetalle> detalles) {
        return detalles.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}
