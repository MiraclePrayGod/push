package org.example.msfacturacion.dato;

import lombok.Data;

import java.util.List;

@Data
public class FacturaRequest {
    private List<Long> ventasIds;          // ventas ya PAGADAS
    private String moneda;                 // PEN, USD
    private String formaPago;              // CONTADO / CREDITO
    private String medioPago;              // EFECTIVO / TARJETA / TRANSFERENCIA
    private String observaciones;

    public List<Long> getVentasIds() {
        return ventasIds;
    }

    public void setVentasIds(List<Long> ventasIds) {
        this.ventasIds = ventasIds;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}