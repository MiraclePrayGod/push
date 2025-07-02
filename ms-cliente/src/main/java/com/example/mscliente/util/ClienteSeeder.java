package com.example.mscliente.util;

import com.example.mscliente.entity.Cliente;
import com.example.mscliente.repository.ClienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ClienteSeeder implements CommandLineRunner {

    private final ClienteRepository clienteRepository;

    public ClienteSeeder(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void run(String... args) {
        if (clienteRepository.count() == 0) {
            Cliente c1 = new Cliente(null, "Juan", "Perez", "DNI", "12345678",
                    "juan@gmail.com", "987654321", "Lima, Perú", true, LocalDateTime.now());
            Cliente c2 = new Cliente(null, "Empresa SAC", "", "RUC", "20123456789",
                    "ventas@empresa.com", "012345678", "Av. Industrial 123", true, LocalDateTime.now());
            clienteRepository.save(c1);
            clienteRepository.save(c2);
        }
    }
}