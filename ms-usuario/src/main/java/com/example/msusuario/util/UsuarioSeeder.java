package com.example.msusuario.util;

import com.example.msusuario.entity.Usuario;
import com.example.msusuario.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UsuarioSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    public UsuarioSeeder(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            Usuario u1 = new Usuario(null, "admin", "admin123", "Admin", "Principal",
                    "ADMIN", true, LocalDateTime.now());
            Usuario u2 = new Usuario(null, "jlopez", "empleado456", "Juan", "Lopez",
                    "TRABAJADOR", true, LocalDateTime.now());
            usuarioRepository.save(u1);
            usuarioRepository.save(u2);
        }
    }
}