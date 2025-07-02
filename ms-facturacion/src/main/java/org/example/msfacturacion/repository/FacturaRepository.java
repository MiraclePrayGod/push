package org.example.msfacturacion.repository;


import org.example.msfacturacion.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findByClienteId(Long clienteId);
}