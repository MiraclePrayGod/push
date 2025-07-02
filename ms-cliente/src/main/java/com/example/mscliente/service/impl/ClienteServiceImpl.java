package com.example.mscliente.service.impl;

import com.example.mscliente.entity.Cliente;
import com.example.mscliente.repository.ClienteRepository;
import com.example.mscliente.service.ClienteService;
import com.example.mscliente.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> buscar(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        if (existePorNumeroDocumento(cliente.getNumeroDocumento())) {
            throw new RuntimeException("El cliente ya está registrado con ese número de documento.");
        }
        cliente.setFechaRegistro(LocalDateTime.now());
        cliente.setEstado(true);
        cliente.setNombres(StringUtils.capitalizarNombre(cliente.getNombres()));
        cliente.setDireccion(cliente.getDireccion());
        cliente.setCorreo(StringUtils.esCorreoValido(cliente.getCorreo()));
        cliente.setTelefono(cliente.getTelefono());
        return clienteRepository.save(cliente);

    }

    @Override
    public Cliente actualizar(Long id, Cliente cliente) {
        cliente.setId(id);


//        if (!clienteRepository.existsById(id)){
//            throw new IllegalArgumentException("Cliente con referencia "+id+" no existe");
//        }
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente con referencia id : "+
                        id + " no existe "));

        clienteExistente.setDireccion( cliente.getDireccion());
        cliente.setCorreo(StringUtils.esCorreoValido(cliente.getCorreo()));
        clienteExistente.setCorreo(StringUtils.esCorreoValido(cliente.getCorreo()));
        clienteExistente.setTelefono(cliente.getTelefono());
        clienteExistente.setEstado(cliente.getEstado());
        clienteExistente.setNombres(StringUtils.capitalizarNombre(cliente.getNombres()));
        clienteExistente.setNumeroDocumento(cliente.getNumeroDocumento());

        return clienteRepository.save(clienteExistente);

    }

    @Override
    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public boolean existePorNumeroDocumento(String numeroDocumento) {
        return clienteRepository.existsByNumeroDocumento(numeroDocumento);
    }

    @Override
    public Cliente habilitarCliente(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()){
            Cliente cliente1 = cliente.get();
            cliente1.setEstado(true);
            clienteRepository.save(cliente1);
            return cliente1;

        }else{
            throw new IllegalArgumentException("El cliente con id " + id + " no ha sido encontrado");
        }
    }
}