package org.example.msventa.entity;

import jakarta.persistence.*;
import org.example.msventa.dato.Cliente;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer clienteId;
    private Integer trabajadorId;      // puede ser null

    private LocalDate fechaVenta;
    private Double total;

    /**  CLIENTE | TRABAJADOR  */
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'CLIENTE'")
    private String origen;

    /**  SIN_PAGAR | PAGADA | FACTURADA  */
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'SIN_PAGAR'")
    private String estado;
    @Column(nullable = true)
    private String estadoLicencia;

    /* ---------- Campos auxiliares ---------- */
    @Transient
    private Cliente cliente;

    @Transient
    private String observacion;

    /* ---------- Relación con detalles ---------- */
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<VentaDetalle> detalles;

    public String getEstadoLicencia() {
        return estadoLicencia == null ? "SIN_NADA" : estadoLicencia;

    }

    public void setEstadoLicencia(String estadoLicencia) {
        this.estadoLicencia = estadoLicencia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Integer getTrabajadorId() {
        return trabajadorId;
    }

    public void setTrabajadorId(Integer trabajadorId) {
        this.trabajadorId = trabajadorId;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<VentaDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<VentaDetalle> detalles) {
        this.detalles = detalles;
    }
    /* ---------- Getters y Setters ---------- */
    /* ... (idénticos a los que ya tenías) ... */
}