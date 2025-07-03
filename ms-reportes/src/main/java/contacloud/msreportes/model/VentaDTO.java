package contacloud.msreportes.model;

import java.time.LocalDate;

public class VentaDTO {
    private Long id;
    private Long clienteId;
    private LocalDate fechaVenta;
    private Double total;
    private String estado;
    // Puedes agregar más campos si lo necesitas

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public LocalDate getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDate fechaVenta) { this.fechaVenta = fechaVenta; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
