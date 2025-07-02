package contacloud.dplicencias.feign;

import contacloud.dplicencias.dto.ClienteDto;
import contacloud.dplicencias.dto.VentaDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ms-venta-service", path = "/ventas")
public interface VentaFeing {
        @GetMapping("/{id}")
        ResponseEntity<VentaDto> obtenerPorId(@PathVariable Integer id);

        @GetMapping("/pagadas/{clienteId}")
        ResponseEntity<List<VentaDto>> pagadas(@PathVariable Integer clienteId);

        @GetMapping("/clientes/{id}")
        ResponseEntity<List<VentaDto>> obtenerByCliente(@PathVariable Integer id);

    @PutMapping("/{id}")
        ResponseEntity<VentaDto> actualizarVenta(@PathVariable Long id, @RequestBody ClienteDto cursoDto);

}
