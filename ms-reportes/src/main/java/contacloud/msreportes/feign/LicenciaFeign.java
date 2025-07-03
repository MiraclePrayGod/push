package contacloud.msreportes.feign;

import contacloud.msreportes.model.LicenciaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "dp-licencias", path = "/licencias")
public interface LicenciaFeign {
    @GetMapping
    ResponseEntity<List<LicenciaDTO>> listarLicencias();
}
