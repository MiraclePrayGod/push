package org.example.msfacturacion.feign;

import org.example.msfacturacion.dato.ProductoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-producto-service", path = "/productos")
public interface ProductoFeign {
    @GetMapping("/{id}")  // ✅ Evita duplicar el path
    ProductoDTO obtener(@PathVariable("id") Long id);
}
