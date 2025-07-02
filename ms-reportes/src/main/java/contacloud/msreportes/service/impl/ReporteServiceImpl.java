package contacloud.msreportes.service.impl;

import contacloud.msreportes.feign.LicenciaFeign;
import contacloud.msreportes.feign.PagoFeign;
import contacloud.msreportes.feign.ProductoFeign;
import contacloud.msreportes.feign.VentaFeign;
import contacloud.msreportes.model.LicenciaDTO;
import contacloud.msreportes.model.PagoDTO;
import contacloud.msreportes.model.ProductoDTO;
import contacloud.msreportes.model.VentaDTO;
import contacloud.msreportes.service.ReporteService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final VentaFeign ventaFeign;
    private final ProductoFeign productoFeign;
    private final PagoFeign pagoFeign;
    private final LicenciaFeign licenciaFeign;

    public ReporteServiceImpl(VentaFeign ventaFeign, ProductoFeign productoFeign, PagoFeign pagoFeign, LicenciaFeign licenciaFeign) {
        this.ventaFeign = ventaFeign;
        this.productoFeign = productoFeign;
        this.pagoFeign = pagoFeign;
        this.licenciaFeign = licenciaFeign;
    }

    @Override
    public void exportarExcel(OutputStream os) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            // Hoja de Ventas
            Sheet ventasSheet = workbook.createSheet("Ventas");
            List<VentaDTO> ventas = ventaFeign.listarVentas().getBody();
            Row headerVentas = ventasSheet.createRow(0);
            headerVentas.createCell(0).setCellValue("ID");
            headerVentas.createCell(1).setCellValue("ClienteID");
            headerVentas.createCell(2).setCellValue("Total");
            headerVentas.createCell(3).setCellValue("Estado");
            int rowIdx = 1;
            for (VentaDTO v : ventas) {
                Row row = ventasSheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(v.getId());
                row.createCell(1).setCellValue(v.getClienteId());
                row.createCell(2).setCellValue(v.getTotal());
                row.createCell(3).setCellValue(v.getEstado());
            }

            // Hoja de Productos
            Sheet productosSheet = workbook.createSheet("Productos");
            List<ProductoDTO> productos = productoFeign.listarProductos().getBody();
            Row headerProd = productosSheet.createRow(0);
            headerProd.createCell(0).setCellValue("ID");
            headerProd.createCell(1).setCellValue("Nombre");
            headerProd.createCell(2).setCellValue("Stock");
            int prodIdx = 1;
            for (ProductoDTO p : productos) {
                Row row = productosSheet.createRow(prodIdx++);
                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getNombre());
                row.createCell(2).setCellValue(p.getStock());
            }

            // Hoja de Pagos
            Sheet pagosSheet = workbook.createSheet("Pagos");
            List<PagoDTO> pagos = pagoFeign.listarPagos().getBody();
            Row headerPago = pagosSheet.createRow(0);
            headerPago.createCell(0).setCellValue("ID");
            headerPago.createCell(1).setCellValue("VentaID");
            headerPago.createCell(2).setCellValue("Monto");
            headerPago.createCell(3).setCellValue("Metodo");
            int pagoIdx = 1;
            for (PagoDTO p : pagos) {
                Row row = pagosSheet.createRow(pagoIdx++);
                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getVentaId());
                row.createCell(2).setCellValue(p.getMonto());
                row.createCell(3).setCellValue(p.getMetodo());
            }

            // Hoja de Licencias
            Sheet licenciasSheet = workbook.createSheet("Licencias");
            List<LicenciaDTO> licencias = licenciaFeign.listarLicencias().getBody();
            Row headerLic = licenciasSheet.createRow(0);
            headerLic.createCell(0).setCellValue("ID");
            headerLic.createCell(1).setCellValue("ClienteID");
            headerLic.createCell(2).setCellValue("Tipo");
            headerLic.createCell(3).setCellValue("Estado");
            int licIdx = 1;
            for (LicenciaDTO l : licencias) {
                Row row = licenciasSheet.createRow(licIdx++);
                row.createCell(0).setCellValue(l.getId());
                row.createCell(1).setCellValue(l.getClienteId());
                row.createCell(2).setCellValue(l.getTipoLicencia());
                row.createCell(3).setCellValue(l.getEstado());
            }

            workbook.write(os);
        }
    }
}
