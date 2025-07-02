package contacloud.msreportes.service;

import java.io.OutputStream;

public interface ReporteService {
    void exportarExcel(OutputStream os) throws Exception;
}
