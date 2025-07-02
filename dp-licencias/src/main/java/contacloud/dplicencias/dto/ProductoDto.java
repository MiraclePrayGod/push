package contacloud.dplicencias.dto;

public class ProductoDto {
    private  Long id;
    private String nombre;

    public ProductoDto() {
    }

    public ProductoDto(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public ProductoDto(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
