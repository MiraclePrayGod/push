package org.example.msfacturacion.feign;

import org.example.msfacturacion.dato.DatosClienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-cliente-service", path = "/clientes")
public interface ClienteFeign {
    @GetMapping("/{id}")
    ResponseEntity<DatosClienteDTO> obtener(@PathVariable Long id);
}