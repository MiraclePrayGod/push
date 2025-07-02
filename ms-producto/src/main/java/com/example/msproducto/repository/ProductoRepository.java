package com.example.msproducto.repository;

import com.example.msproducto.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByNombre(String nombre);
}