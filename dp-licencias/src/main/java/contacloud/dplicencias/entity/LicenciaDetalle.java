package contacloud.dplicencias.entity;


import contacloud.dplicencias.dto.ProductoDto;
import contacloud.dplicencias.dto.VentaDto;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class LicenciaDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer detalleId;
    private Integer ventaId;
    private Long productoId;
    private String codigoLicencia;  // Código único de la licencia
    private String contrasena;
    @Transient
    private VentaDto ventaDto;
    @Transient
    private ProductoDto productoDto;


    public LicenciaDetalle() {
    }

    public LicenciaDetalle(Integer detalleId,
                           Integer   ventaId,
                           Long productoId,
                           String codigoLicencia,
                           String contrasena,
                           VentaDto ventaDto,
                           ProductoDto productoDto) {
        this.detalleId = detalleId;
        this.ventaId = ventaId;
        this.productoId = productoId;
        this.codigoLicencia = codigoLicencia;
        this.contrasena = contrasena;
        this.ventaDto = ventaDto;
        this.productoDto = productoDto;
    }

    public Integer getDetalleId() {
        return detalleId;
    }

    public void setDetalleId(Integer detalleId) {
        this.detalleId = detalleId;
    }

    public Integer getVentaId() {
        return ventaId;
    }

    public void setVentaId(Integer ventaId) {
        this.ventaId = ventaId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getCodigoLicencia() {
        return codigoLicencia;
    }

    public void setCodigoLicencia(String codigoLicencia) {
        this.codigoLicencia = codigoLicencia;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public VentaDto getVentaDto() {
        return ventaDto;
    }

    public void setVentaDto(VentaDto ventaDto) {
        this.ventaDto = ventaDto;
    }

    public ProductoDto getProductoDto() {
        return productoDto;
    }

    public void setProductoDto(ProductoDto productoDto) {
        this.productoDto = productoDto;
    }
}
