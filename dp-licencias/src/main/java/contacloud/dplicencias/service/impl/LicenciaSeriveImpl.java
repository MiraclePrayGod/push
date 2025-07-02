package contacloud.dplicencias.service.impl;

import contacloud.dplicencias.dto.*;
import contacloud.dplicencias.entity.Licencia;
import contacloud.dplicencias.entity.LicenciaDetalle;
import contacloud.dplicencias.feign.ClienteFeing;
import contacloud.dplicencias.feign.ProductoFeing;
import contacloud.dplicencias.feign.VentaFeing;
import contacloud.dplicencias.repository.LicenciaRepository;
import contacloud.dplicencias.service.LicenciaService;
import contacloud.dplicencias.util.StringUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LicenciaSeriveImpl implements LicenciaService {

    @Autowired
    private LicenciaRepository licenciaRepository;

    @Autowired
    private ClienteFeing clienteFeing;

    @Autowired
    private VentaFeing ventaFeing;

    @Autowired
    private ProductoFeing productoFeing;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private JavaMailSenderImpl mailSender;



    // Método listar sin CircuitBreaker
    @Override
    public List<Licencia> listar() {
        List<Licencia> licencias = licenciaRepository.findAll();
        for (Licencia licencia : licencias) {
            ClienteDto clienteDto = clienteFeing.obtenerPorId(licencia.getClienteId()).getBody();
            licencia.setClienteDto(clienteDto);
            for (LicenciaDetalle detalle: licencia.getDetalles()) {
                VentaDto ventaDto = ventaFeing.obtenerPorId(detalle.getVentaId()).getBody();
                ProductoDto productoDto = productoFeing.obtenerProductoPorId(detalle.getProductoId()).getBody();
                if (ventaDto == null ) {
                    throw new DataIntegrityViolationException("Venta con ese id "+ ventaDto.getId()+" no existe ");
                }else {
                    detalle.setVentaDto(ventaDto);
                }
                if (productoDto == null){
                    throw new DataIntegrityViolationException("Producto con ese id "+ productoDto.getId()+" no existe ");
                }else {
                    detalle.setProductoDto(productoDto);
                }
            }
        }
        return licencias;
    }

    // Método buscar sin CircuitBreaker
    @Override
    public Optional<Licencia> buscar(Integer id) {
        Optional<Licencia> optionalLicencia = licenciaRepository.findById(id);

        optionalLicencia.ifPresent(licencia -> {
            ClienteDto clienteDto = clienteFeing.obtenerPorId(licencia.getClienteId()).getBody();
            licencia.setClienteDto(clienteDto);
            for (LicenciaDetalle detalle: licencia.getDetalles()) {
                VentaDto ventaDto = ventaFeing.obtenerPorId(detalle.getVentaId()).getBody();
                ProductoDto productoDto =  productoFeing.obtenerProductoPorId(detalle.getProductoId()).getBody();
                detalle.setVentaDto(ventaDto);
                detalle.setProductoDto(productoDto);
            }
        });

        return optionalLicencia;
    }

    // Método guardar sin CircuitBreaker
    @Override
    public Licencia guardar(Licencia licencia) {

        ClienteDto cliente = null;
        try {
            // Intentamos obtener el cliente
            cliente = clienteFeing.obtenerPorId(licencia.getClienteId()).getBody();
            // Si el cliente es nulo, lanzamos una excepción personalizada
            if (cliente == null) {
                throw new RuntimeException("Cliente no encontrado con ID: " + licencia.getClienteId());
            }
        } catch (RuntimeException e) {
            // Capturamos y mostramos el tipo de error en caso de excepciones específicas
            System.out.println("Error tipo: " + e.getClass().getSimpleName());
            System.out.println("Mensaje de error: " + e.getMessage());
            throw e;  // Vuelve a lanzar la excepción para manejarla más arriba si es necesario
        } catch (Exception e) {
            // Captura cualquier otra excepción que pueda ocurrir
            System.out.println("Error general tipo: " + e.getClass().getSimpleName());
            System.out.println("Mensaje de error: " + e.getMessage());
            e.printStackTrace();  // Imprime el stack trace para más detalles
            throw new RuntimeException("Error inesperado: " + e.getMessage(), e);  // Lanza una excepción general
        }

        licencia.setClienteId(licencia.getClienteId());
        licencia.setTipoLicencia(licencia.getTipoLicencia());
        licencia.setFechaActivacion(LocalDate.now());
        licencia.setFechaExpiracion(licencia.getFechaExpiracion());
        licencia.setEstado(true);
        licencia.setClienteDto(cliente);

        List<VentaDto> ventaDto = null;
        try {
            ventaDto = ventaFeing.pagadas(cliente.getId().intValue()).getBody();
            if (ventaDto == null || ventaDto.isEmpty()) {
                throw new RuntimeException("No se encontraron ventas para el cliente con ID: " + cliente.getId());
            }
        } catch (Exception e) {
            System.out.println("Error tipo: " + e.getClass().getSimpleName());
            System.out.println("Mensaje de error: " + e.getMessage());
            e.printStackTrace();
        }

        List<LicenciaDetalle> detallesLicencia = new ArrayList<>();
        for (VentaDto ventaDto1: ventaDto) {
            for ( VentaDetalleDto detalleDto : ventaDto1.getDetalles()) {
                ProductoDto productoDto = productoFeing.obtenerProductoPorId(detalleDto.getProductoId()).getBody();
                if (productoDto == null) {
                    throw new RuntimeException("Producto no encontrado con ID: " + detalleDto.getProductoId());
                }

                LicenciaDetalle detalle = new LicenciaDetalle();

                detalle.setVentaId(ventaDto1.getId());
                detalle.setProductoId(detalleDto.getId().longValue());
                detalle.setCodigoLicencia(StringUtils.generarCodigoLicencia(cliente.getNombres()));
                detalle.setContrasena(StringUtils.generarContrasena(cliente.getNombres()));
                detalle.setVentaDto(ventaDto1);
                detalle.setProductoDto(productoDto);
                // Agregar el detalle a la lista
                detallesLicencia.add(detalle);
            }


        }

        licencia.setDetalles(detallesLicencia);
        Licencia nuevaLicencia = licenciaRepository.save(licencia);
        sendEmail(licencia.getClienteId().intValue());
        return nuevaLicencia;
    }

    @Override
    public Licencia actualizar(Integer id, Licencia licencia) {
        Licencia licenciaExistente = licenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Licencia no encontrada con ID: " + id));
        return licenciaRepository.save(licencia);
    }

    @Override
    public void eliminar(Integer id) {
        Licencia licencia = licenciaRepository.getById(id);
        List<VentaDto> ventaDto = ventaFeing.obtenerByCliente(licencia.getClienteId().intValue()).getBody();
        if (ventaDto.isEmpty()) {
            throw new DataIntegrityViolationException("No se encontraron ventas con ese el id " + id + " del cliente");
        }
        ClienteDto clienteDto = null;
        for (VentaDto venta : ventaDto) {
            clienteDto = clienteFeing.obtenerPorId(licencia.getClienteId().longValue()).getBody();

            for (VentaDetalleDto detalle : venta.getDetalles()) {
                ProductoDto productoDto = productoFeing.obtenerProductoPorId(detalle.getProductoId()).getBody();
                detalle.setProductoId(productoDto.getId());
            }
        }
        if (clienteDto.getEstado().equals("HABILITADO")) {
            clienteDto.setEstado("INHABILITADO");
        }
        licenciaRepository.deleteById(id);
    }

    @Override
    public String sendEmail(Integer clienteId) {
        ClienteDto clienteDto = clienteFeing.obtenerPorId(clienteId.longValue()).getBody();
      String email = clienteDto.getCorreo();

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("El cliente no tiene un correo electrónico válido.");
        }

        try {
            String codigo = "NO DISPONIBLE";
            String contrasena = "NO DISPONIBLE";
            String nombreCliente = clienteDto.getNombres();

            List<Licencia> licencias = licenciaRepository.findByClienteId(clienteId);

            if (!licencias.isEmpty()) {
                for (Licencia licencia : licencias) {
                    if (licencia.getDetalles() != null && !licencia.getDetalles().isEmpty()) {
                        LicenciaDetalle licenciaDetalle = licencia.getDetalles().get(0);
                        codigo = licenciaDetalle.getCodigoLicencia();
                        contrasena = licenciaDetalle.getContrasena();
                    } else {
                        throw new RuntimeException("Licencia no tiene detalles asociados.");
                    }

                    Context context = new Context();
                    context.setVariable("codigo", codigo);
                    context.setVariable("contrasena", contrasena);
                    context.setVariable("cliente", nombreCliente);

                    String htmlContent = templateEngine.process("email/licencia-activa", context);

                    MimeMessage mimeMessage = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                    helper.setFrom("reginaldomayhuire@upeu.edu.pe");
                    helper.setTo(email);
                    helper.setSubject("LICENCIA DE SOFTWARE");
                    helper.setText(htmlContent, true);

                    mailSender.send(mimeMessage);
                }

                return "Correo enviado correctamente a "+ email;
            } else {
                throw new RuntimeException("No se encontraron licencias para el cliente con ID: " + clienteId);
            }

        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage());
        }
    }

    @Override
    public List<Licencia> buscarIdCLiente(Integer clienteId) {
        List<Licencia> licencias = licenciaRepository.findByClienteId(clienteId);

        for (Licencia licencia : licencias) {
            ClienteDto clienteDto = clienteFeing.obtenerPorId(licencia.getClienteId()).getBody();
            licencia.setClienteDto(clienteDto);
            for (LicenciaDetalle detalle: licencia.getDetalles()) {
                VentaDto ventaDto = ventaFeing.obtenerPorId(detalle.getVentaId()).getBody();
                ProductoDto productoDto = productoFeing.obtenerProductoPorId(detalle.getProductoId()).getBody();
                if (ventaDto == null ) {
                    throw new DataIntegrityViolationException("Venta con ese id "+ ventaDto.getId()+" no existe ");
                }else {
                    detalle.setVentaDto(ventaDto);
                }
                if (productoDto == null){
                    throw new DataIntegrityViolationException("Producto con ese id "+ productoDto.getId()+" no existe ");
                }else {
                    detalle.setProductoDto(productoDto);
                }
            }
        }
        return licencias;

    }


    @Override
    public Licencia renovarLicencia(Integer id) {
        Licencia licencia = licenciaRepository.getByClienteId(id.longValue());
        licencia.setEstado(true);
        guardar(licencia);
        return licencia;
    }

    @Override
    public Licencia licenciaExpirada(Integer id) {
        Licencia licencia = licenciaRepository.getById(id);
        if (licencia.getFechaExpiracion().isEqual(LocalDate.now())) {
            licencia.setEstado(false);
        }
            return licenciaRepository.save(licencia);
    }

}
