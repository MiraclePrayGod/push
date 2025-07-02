package contacloud.msreportes.feign;

import contacloud.msreportes.model.PagoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "ms-pago-service", path = "/pagos")
public interface PagoFeign {
    @GetMapping
    ResponseEntity<List<PagoDTO>> listarPagos();
}
