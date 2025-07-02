package org.example.msventa.repository;

import org.example.msventa.entity.Venta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Integer> {

    List<Venta> findByClienteId(Integer clienteId);

    // 🔹 ventas pendientes de un cliente
    List<Venta> findByClienteIdAndEstado(Integer clienteId, String estado);

    // 🔹 buscar venta SIN_PAGAR por id
    Optional<Venta> findByIdAndEstado(Integer id, String estado);

    List<Venta> getByClienteId(Integer clienteId);
    List<Venta> findByClienteIdAndEstadoAndEstadoLicenciaIn(Integer clienteId, String estado, List<String> estadoLicencia);

    List<Venta> findByEstadoAndEstadoLicenciaIn(String estado, List<String> estadoLicencia);


}