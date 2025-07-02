package contacloud.dplicencias.dto;

import java.time.LocalDate;
import java.util.List;

public class VentaDto {
    private int id;
    private String clienteId;
    private LocalDate fechaVenta;
    private String estado;
    private List<VentaDetalleDto> detalles;


    public VentaDto() {
    }

    public VentaDto(int id, String clienteId, LocalDate fechaVenta, String estado, List<VentaDetalleDto> detalles) {
        this.id = id;
        this.clienteId = clienteId;
        this.fechaVenta = fechaVenta;
        this.estado = estado;
        this.detalles=detalles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaCompra) {
        this.fechaVenta = fechaCompra;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<VentaDetalleDto> getDetalles() {
        return detalles;
    }
    public void setDetalles(List<VentaDetalleDto> detalles) {
        this.detalles = detalles;
    }
}