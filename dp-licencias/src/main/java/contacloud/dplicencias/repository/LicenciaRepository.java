package contacloud.dplicencias.repository;

import contacloud.dplicencias.entity.Licencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LicenciaRepository extends JpaRepository<Licencia, Integer> {

    List<Licencia> findByClienteId(Integer clienteId);

    Licencia getByEstado(Boolean estado);

    Licencia getByClienteId(Long clienteId);
}
