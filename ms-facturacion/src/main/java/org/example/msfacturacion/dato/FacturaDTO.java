package org.example.msfacturacion.dato;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
public class FacturaDTO {
    private Long id;
    private String numeroFactura;
    private LocalDate fechaEmision;
    private String moneda;
    private String tipoComprobante;        // FACTURA
    private String formaPago;
    private String medioPago;
    private DatosClienteDTO cliente;
    private List<FacturaDetalleDTO> items;
    private Double subTotal;
    private Double totalImpuestos;
    private Double total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public String getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }

    public DatosClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(DatosClienteDTO cliente) {
        this.cliente = cliente;
    }

    public List<FacturaDetalleDTO> getItems() {
        return items;
    }

    public void setItems(List<FacturaDetalleDTO> items) {
        this.items = items;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getTotalImpuestos() {
        return totalImpuestos;
    }

    public void setTotalImpuestos(Double totalImpuestos) {
        this.totalImpuestos = totalImpuestos;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}