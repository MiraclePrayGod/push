package org.example.msfacturacion.feign;

import org.example.msfacturacion.dato.VentaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(name = "ms-venta-service", path = "/ventas")
public interface VentaFeign {
    @GetMapping("/pagadas/{clienteId}")
    ResponseEntity<List<VentaDTO>> pagadas(@PathVariable Long clienteId);

    @GetMapping("/{id}")
    ResponseEntity<VentaDTO> obtener(@PathVariable Long id);

    @PutMapping("/{id}/marcar-facturada")
    ResponseEntity<Void> facturar(@PathVariable Long id);
}