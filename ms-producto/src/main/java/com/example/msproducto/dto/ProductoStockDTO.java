package com.example.msproducto.dto;



public class ProductoStockDTO {
    private Integer stock;

    public ProductoStockDTO() {}

    public ProductoStockDTO(Integer stock) {
        this.stock = stock;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}