package com.example.msproducto.service;

import com.example.msproducto.entity.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {

    List<Producto> listar();

    Optional<Producto> buscar(Long id);

    Producto guardar(Producto producto);

    Producto actualizar(Long id, Producto producto);

    void eliminar(Long id);


    boolean existePorNombre(String nombre);
    // Validación de DNI único
}
