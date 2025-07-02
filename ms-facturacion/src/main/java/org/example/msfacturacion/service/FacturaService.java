package org.example.msfacturacion.service;


import org.example.msfacturacion.dato.FacturaDTO;
import org.example.msfacturacion.dato.FacturaRequest;
import org.example.msfacturacion.dato.VentaDTO;
import org.example.msfacturacion.entity.Factura;

import java.util.List;

public interface FacturaService {
    List<VentaDTO> ventasPagadas(Long clienteId);
    FacturaDTO emitir(FacturaRequest request);
    List<FacturaDTO> listarPorCliente(Long clienteId);
    FacturaDTO convertirADTO(Factura factura);

}