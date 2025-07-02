package com.example.mspago.feign;

import com.example.mspago.dto.ProductoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ms-producto-service", path = "/productos")
public interface ProductoFeign {

    @GetMapping("/{id}")
    ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Long id);

    @PutMapping("/{id}")
    ResponseEntity<ProductoDTO> actualizar(@PathVariable Long id, @RequestBody ProductoDTO productoDto);

}