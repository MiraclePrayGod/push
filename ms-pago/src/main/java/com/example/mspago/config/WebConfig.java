package com.example.mspago.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Actualiza la ruta a la ubicación deseada de los comprobantes
        String comprobantesPath = "file:///C:/Users/touse/Documents/push/ms-pago/comprobantes/";

        // Registrar la ruta de los recursos
        registry.addResourceHandler("/comprobantes/**")
                .addResourceLocations(comprobantesPath);
    }
}
