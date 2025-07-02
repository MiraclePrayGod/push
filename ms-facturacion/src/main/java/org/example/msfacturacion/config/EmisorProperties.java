package org.example.msfacturacion.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "factura.emisor")
@Data
public class EmisorProperties {
    private String ruc;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
}