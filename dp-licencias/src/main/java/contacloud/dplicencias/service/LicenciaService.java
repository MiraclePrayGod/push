package contacloud.dplicencias.service;

import contacloud.dplicencias.entity.Licencia;

import java.util.List;
import java.util.Optional;

public interface LicenciaService {

    List<Licencia> listar();
    Optional<Licencia> buscar(Integer id);
    Licencia guardar(Licencia licenciaDato);
    Licencia actualizar(Integer id,Licencia licencia);
    void eliminar(Integer id);
    String sendEmail(Integer clienteId);
    List<Licencia> buscarIdCLiente(Integer ClienteId);
    Licencia renovarLicencia(Integer id);
    Licencia licenciaExpirada(Integer id);

}
