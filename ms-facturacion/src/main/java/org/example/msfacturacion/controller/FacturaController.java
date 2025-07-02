package org.example.msfacturacion.controller;

import org.example.msfacturacion.dato.FacturaDTO;
import org.example.msfacturacion.dato.FacturaRequest;
import org.example.msfacturacion.dato.VentaDTO;
import org.example.msfacturacion.entity.Factura;
import org.example.msfacturacion.repository.FacturaRepository;
import org.example.msfacturacion.service.FacturaService;
import org.example.msfacturacion.service.PdfGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    private final FacturaService facturaService;
    private final PdfGeneratorService pdfGeneratorService;
    private final FacturaRepository facturaRepository;

    @Autowired
    public FacturaController(FacturaService facturaService,
                             PdfGeneratorService pdfGeneratorService,
                             FacturaRepository facturaRepository) {
        this.facturaService       = facturaService;
        this.pdfGeneratorService  = pdfGeneratorService;
        this.facturaRepository    = facturaRepository;
    }

    /* ────────────────────────────────────────────────────────────────
       1. Listar las ventas PAGADAS de un cliente (para seleccionar) */
    @GetMapping("/ventas-pagadas/{clienteId}")
    public List<VentaDTO> pagadas(@PathVariable Long clienteId) {
        return facturaService.ventasPagadas(clienteId);
    }

    /* ────────────────────────────────────────────────────────────────
       2. Emitir una factura con una o varias ventas  */
    @PostMapping
    public ResponseEntity<FacturaDTO> emitir(@RequestBody FacturaRequest req) {
        return ResponseEntity.ok(facturaService.emitir(req));
    }

    /* ────────────────────────────────────────────────────────────────
       3. Listar facturas emitidas por cliente                        */
    @GetMapping("/cliente/{clienteId}")
    public List<FacturaDTO> porCliente(@PathVariable Long clienteId) {
        return facturaService.listarPorCliente(clienteId);
    }

    /* ────────────────────────────────────────────────────────────────
       4. Descargar la factura en formato PDF                         */
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarFacturaPdf(@PathVariable Long id) {

        // a) recuperar la entidad factura
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        // b) mapear a DTO con el método ya existente en tu service
        FacturaDTO dto = facturaService.convertirADTO(factura);

        // c) generar el PDF en memoria
        byte[] pdfBytes = pdfGeneratorService.generarFacturaPdf(dto);

        // d) construir la respuesta HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=factura-" + dto.getNumeroFactura() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}