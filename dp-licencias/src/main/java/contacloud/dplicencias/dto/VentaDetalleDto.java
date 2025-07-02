package contacloud.dplicencias.dto;

import jakarta.persistence.Transient;

public class VentaDetalleDto {
    private Integer id;
    private Long productoId;

    public VentaDetalleDto(Integer id, Long productoId) {
        this.id = id;
        this.productoId = productoId;
    }

    public VentaDetalleDto() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

}
