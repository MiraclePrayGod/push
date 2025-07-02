/*======================================================================
 *  src/main/java/org/example/msfacturacion/entity/FacturaDetalle.java
 *======================================================================*/
package org.example.msfacturacion.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Detalle de factura: cada ítem facturado.
 *
 * ⚠  CORRECCIÓN: se elimina el mapeo duplicado de la columna
 *    factura_id.  Ahora **solo** existe la relación @ManyToOne.
 *    Si necesitas el id de la factura, usa getFactura().getId().
 */
@Entity
@Table(name = "factura_detalle")
public class FacturaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ---------------- FK a la tabla factura ---------------- */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)   // <- única referencia
    private Factura factura;

    /* ---------------- Otras columnas persistentes ---------- */
    private Long ventaId;          // traqueo a la venta original
    private Long productoId;

    private String descripcion;
    private Integer cantidad;
    private String unidadMedida;
    private Double precioUnitario;
    private Double subtotal;
    private Double igv;
    private Double totalLinea;
    private String nombreProducto;
    @Transient
    private LocalDate fechaVenta;

    /* ---------------- Getters / Setters -------------------- */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }

    public Long getVentaId() { return ventaId; }
    public void setVentaId(Long ventaId) { this.ventaId = ventaId; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getIgv() { return igv; }
    public void setIgv(Double igv) { this.igv = igv; }

    public Double getTotalLinea() { return totalLinea; }
    public void setTotalLinea(Double totalLinea) { this.totalLinea = totalLinea; }

    /* ----------- Convenience: obtener el ID de factura ------ */
    @Transient
    public Long getFacturaId() {
        return (factura != null) ? factura.getId() : null;
    }
}
