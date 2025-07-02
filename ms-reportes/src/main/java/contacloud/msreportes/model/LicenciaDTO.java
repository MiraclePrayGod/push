package contacloud.msreportes.model;

public class LicenciaDTO {
    private Long id;
    private Long clienteId;
    private String tipoLicencia;
    private Boolean estado;
    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public String getTipoLicencia() { return tipoLicencia; }
    public void setTipoLicencia(String tipoLicencia) { this.tipoLicencia = tipoLicencia; }
    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
}
