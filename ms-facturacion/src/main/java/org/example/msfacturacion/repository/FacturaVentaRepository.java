package org.example.msfacturacion.repository;


import org.example.msfacturacion.entity.FacturaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacturaVentaRepository extends JpaRepository<FacturaDetalle, Long> {
    /**
     * Verifica si una venta ya está asociada a alguna factura.
     */
    boolean existsByVentaId(Long ventaId);

    /**
     * Devuelve los registros FacturaVenta que pertenezcan a la lista de IDs de venta indicada.
     */
    List<FacturaDetalle> findByVentaIdIn(List<Long> ventaIds);
}