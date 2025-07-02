package contacloud.dplicencias.feign;


import contacloud.dplicencias.dto.ProductoDto;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-producto-service", path = "/productos")
public interface ProductoFeing {

//    @GetMapping("/{id}")
//    ResponseEntity<ProductoDto> obtenerPorId(@PathVariable Long id);
    @GetMapping("/{id}")
    @CircuitBreaker(name = "productoId" , fallbackMethod = "fallBackProductoById")
    ResponseEntity<ProductoDto> obtenerProductoPorId(@PathVariable Long id);

   default ResponseEntity<ProductoDto> fallBackProductoById( Long id, Throwable e){
        ProductoDto productoDto = new ProductoDto();
            productoDto.setId(-1L);
            productoDto.setNombre("Producto sin nombre");
            return ResponseEntity.ok(productoDto);
    }

}
