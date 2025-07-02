package com.example.mscliente.controller;

import com.example.mscliente.entity.Cliente;
import com.example.mscliente.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        return new ResponseEntity<>(clienteService.listar(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.buscar(id);
        return cliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
        return new ResponseEntity<>(clienteService.guardar(cliente), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        Cliente actualizado = clienteService.actualizar(id, cliente);
        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/habilitar/{clienteId}")
    public ResponseEntity<Void> habilitarCliente(@PathVariable Long clienteId) {
        // Aquí actualizas el estado del cliente a "habilitado"
        clienteService.habilitarCliente(clienteId);
        return ResponseEntity.ok().build();
    }
}