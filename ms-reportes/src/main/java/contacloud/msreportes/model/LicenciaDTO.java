package contacloud.msreportes.model;

import java.time.LocalDate;

public class LicenciaDTO {
    private Long id;
    private Long clienteId;
    private String tipoLicencia;
    private LocalDate fechaActivacion;
    private LocalDate fechaExpiracion;
    private String estado;
    // Puedes agregar más campos si lo necesitas

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public String getTipoLicencia() { return tipoLicencia; }
    public void setTipoLicencia(String tipoLicencia) { this.tipoLicencia = tipoLicencia; }
    public LocalDate getFechaActivacion() { return fechaActivacion; }
    public void setFechaActivacion(LocalDate fechaActivacion) { this.fechaActivacion = fechaActivacion; }
    public LocalDate getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(LocalDate fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
