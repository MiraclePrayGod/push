package com.example.mspago.repository;

import com.example.mspago.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByClienteId(Long clienteId);
}