package com.example.msusuario.service;

import com.example.msusuario.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> listar();
    Optional<Usuario> buscar(Long id);
    Usuario guardar(Usuario usuario);
    Usuario actualizar(Long id, Usuario usuario);
    void eliminar(Long id);
    boolean existePorUsername(String username);
}