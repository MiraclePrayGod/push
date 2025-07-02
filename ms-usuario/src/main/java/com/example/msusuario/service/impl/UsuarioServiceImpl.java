package com.example.msusuario.service.impl;

import com.example.msusuario.entity.Usuario;
import com.example.msusuario.repository.UsuarioRepository;
import com.example.msusuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> buscar(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        if (existePorUsername(usuario.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizar(Long id, Usuario usuario) {
        usuario.setId(id);
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public boolean existePorUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }
}