package contacloud.msreportes.controller;

import contacloud.msreportes.service.ReporteService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/excel")
    public ResponseEntity<byte[]> exportarExcel() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        reporteService.exportarExcel(baos);
        byte[] bytes = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=reporte.xlsx");  // Cambié 'attachment' por 'inline'

        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }

}
