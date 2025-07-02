package com.example.mscliente.service;

import com.example.mscliente.entity.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteService {
    List<Cliente> listar();
    Optional<Cliente> buscar(Long id);
    Cliente guardar(Cliente cliente);
    Cliente actualizar(Long id, Cliente cliente);
    void eliminar(Long id);
    boolean existePorNumeroDocumento(String numeroDocumento);
    Cliente habilitarCliente(Long id);

}