package com.example.mspago.dto;

    public class ProductoDTO {
        private Long id;
        private String nombre;
        private Integer stock;
        private Boolean estado;
        private Double precioUnitario;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public Integer getStock() {
            return stock;
        }

        public void setStock(Integer stock) {
            this.stock = stock;
        }

        public Boolean getEstado() {
            return estado;
        }

        public void setEstado(Boolean estado) {
            this.estado = estado;
        }

        public Double getPrecioUnitario() {
            return precioUnitario;
        }

        public void setPrecioUnitario(Double precioUnitario) {
            this.precioUnitario = precioUnitario;
        }
    }
