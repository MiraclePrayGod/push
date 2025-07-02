package contacloud.dplicencias.controller;


import contacloud.dplicencias.entity.Licencia;
import contacloud.dplicencias.service.LicenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/licencias")
public class LicenciaController {

    @Autowired
    private LicenciaService licenciaService;

    @GetMapping
    public ResponseEntity<List<Licencia>> listarLicencias() {
        List<Licencia> licencias = licenciaService.listar();
        return ResponseEntity.ok(licencias);  // Retorna una lista de licencias
    }

    // Endpoint para buscar una licencia por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Licencia> obtenerLicencia(@PathVariable Integer id) {
        Optional<Licencia> licencia = licenciaService.buscar(id);
        return licencia.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para crear una nueva licencia
    @PostMapping
    public ResponseEntity<Licencia> guardar(@RequestBody Licencia licencia) {
        return new ResponseEntity<>(licenciaService.guardar(licencia),HttpStatus.CREATED);
    }

    // Endpoint para actualizar una licencia existente
    @PutMapping("/{id}")
    public ResponseEntity<Licencia> actualizar(@PathVariable Integer id, @RequestBody Licencia licencia) {
        // Actualizar licencia
        return ResponseEntity.ok(licencia);  // Retorna la licencia actualizada
    }

    // Endpoint para eliminar una licencia por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLicencia(@PathVariable Integer id) {
        licenciaService.eliminar(id);  // Eliminar la licencia
        return ResponseEntity.noContent().build();  // Retorna un código de estado 204 (sin contenido)
    }
    @PostMapping("/enviar/{clienteId}")
    public ResponseEntity<String> sendEmail( @PathVariable Integer clienteId) {
        try {
            // Llamada al servicio que envía el correo
            String token = licenciaService.sendEmail( clienteId);
            // Respuesta exitosa con código 200
            return ResponseEntity.status(200).body("Correo enviado con éxito. Token generado: " + token);
        } catch (Exception e) {
            // Respuesta con código 500 en caso de error
            return ResponseEntity.status(500).body("Error al enviar el correo: " + e.getMessage());
        }
    }

}


