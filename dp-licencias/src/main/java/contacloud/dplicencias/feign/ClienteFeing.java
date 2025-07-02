package contacloud.dplicencias.feign;

import contacloud.dplicencias.dto.ClienteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-cliente-service", path = "/clientes")
public interface ClienteFeing {
        @GetMapping("/{id}")
        ResponseEntity<ClienteDto> obtenerPorId(@PathVariable Long id);

        @PutMapping("/{id}")
        ResponseEntity<ClienteDto> actualizarCliente(@PathVariable Long id, @RequestBody ClienteDto cursoDto);

}
