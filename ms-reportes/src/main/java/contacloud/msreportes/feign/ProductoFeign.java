package contacloud.msreportes.feign;

import contacloud.msreportes.model.ProductoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "ms-producto-service", path = "/productos")
public interface ProductoFeign {
    @GetMapping
    ResponseEntity<List<ProductoDTO>> listarProductos();
}
