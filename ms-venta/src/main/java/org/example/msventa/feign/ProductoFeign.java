package org.example.msventa.feign;

import org.example.msventa.dato.Producto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ms-producto-service", path = "/productos")
public interface ProductoFeign {
    @GetMapping("/{id}")
    ResponseEntity<Producto> obtenerPorId(@PathVariable Integer id);

    @PutMapping("/{id}")
    ResponseEntity<Producto> actualizarProducto(@PathVariable Integer id, @RequestBody Producto producto);
}