    package com.example.mspago.config;

    import org.springframework.context.annotation.Configuration;
    import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
    import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

    @Configuration
    public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/comprobantes/**")
                    .addResourceLocations("file:///E:/oficial examen dad 2025/proyectodad/ms-pago/comprobantes/");
        }

    }
