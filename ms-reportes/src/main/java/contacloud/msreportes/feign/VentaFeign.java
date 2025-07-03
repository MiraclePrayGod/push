package contacloud.msreportes.feign;

import contacloud.msreportes.model.VentaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "ms-venta-service", path = "/ventas")
public interface VentaFeign {
    @GetMapping
    ResponseEntity<List<VentaDTO>> listarVentas();
}
