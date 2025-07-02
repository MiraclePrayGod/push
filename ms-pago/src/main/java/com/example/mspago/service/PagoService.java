package com.example.mspago.service;
import com.example.mspago.dto.PagoRequest;
import com.example.mspago.entity.Pago;
import com.example.mspago.dto.VentaDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PagoService {

    List<VentaDTO> ventasPendientes(Long clienteId);

    Pago registrar(PagoRequest request, MultipartFile comprobante);

    Pago obtener(Long id);
    String obtenerNombreComprobantePorPagoId(Long pagoId);


}
