package com.example.mspago.controller;

import com.example.mspago.dto.PagoRequest;
import com.example.mspago.entity.Pago;
import com.example.mspago.service.PagoService;
import com.example.mspago.dto.VentaDTO;
import com.example.mspago.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    @Autowired private PagoService pagoService;

    @Autowired
    private StorageService storageService;

    /** Lista ventas SIN_PAGAR de un cliente */
    @GetMapping("/pendientes/{clienteId}")
    public ResponseEntity<List<VentaDTO>> pendientes(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pagoService.ventasPendientes(clienteId));
    }
    @GetMapping("/{id}/comprobante-imagen")
    public ResponseEntity<Resource> obtenerComprobanteImagen(@PathVariable Long id) {
        try {
            // Obtener nombre del archivo comprobante
            String nombreArchivo = pagoService.obtenerNombreComprobantePorPagoId(id);
            if (nombreArchivo == null || nombreArchivo.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Ruta completa al archivo en disco (igual a WebConfig)
            Path rutaArchivo = Paths.get("E:/oficial examen dad 2025/proyectodad/ms-pago/comprobantes")
                    .resolve(nombreArchivo)
                    .normalize();

            Resource recurso = new UrlResource(rutaArchivo.toUri());
            if (!recurso.exists()) {
                return ResponseEntity.notFound().build();
            }

            // Definir content type según extensión del archivo
            String contentType = "application/octet-stream";
            if (nombreArchivo.toLowerCase().endsWith(".png")) contentType = "image/png";
            else if (nombreArchivo.toLowerCase().endsWith(".jpg") || nombreArchivo.toLowerCase().endsWith(".jpeg"))
                contentType = "image/jpeg";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + recurso.getFilename() + "\"")
                    .body(recurso);

        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/{id}/comprobante")
    public ResponseEntity<String> obtenerNombreComprobante(@PathVariable Long id) {
        String nombreArchivo = pagoService.obtenerNombreComprobantePorPagoId(id);
        return ResponseEntity.ok(nombreArchivo);
    }

    @GetMapping("/comprobantes")
    public ResponseEntity<List<String>> listarComprobantes() {
        List<String> archivos = storageService.loadAll();
        return ResponseEntity.ok(archivos);
    }


        /** Registra un pago (multipart para comprobante opcional) */
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Pago> pagar(
            @RequestPart("data") PagoRequest request,
            @RequestPart(value = "archivo", required = false) MultipartFile archivo) {

        return ResponseEntity.ok(pagoService.registrar(request, archivo));
    }

    /** Detalle de pago */
    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.obtener(id));
    }
}

