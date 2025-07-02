package com.example.mspago.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ventaId;
    private Long clienteId;
    private Long trabajadorId;            // puede ser null

    @Column(nullable = false, length = 20)
    private String metodo;               // CONTADO | TARJETA | TRANSFERENCIA

    private Double monto;

    private LocalDateTime fechaPago;

    private String comprobanteUrl;        // null salvo transferencia

    @Column(nullable = false, length = 20)
    private String estado = "CONFIRMADO"; // por ahora fijo

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVentaId() {
        return ventaId;
    }

    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getTrabajadorId() {
        return trabajadorId;
    }

    public void setTrabajadorId(Long trabajadorId) {
        this.trabajadorId = trabajadorId;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getComprobanteUrl() {
        return comprobanteUrl;
    }

    public void setComprobanteUrl(String comprobanteUrl) {
        this.comprobanteUrl = comprobanteUrl;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}