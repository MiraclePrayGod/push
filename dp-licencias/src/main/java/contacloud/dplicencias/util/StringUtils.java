package contacloud.dplicencias.util;

import contacloud.dplicencias.dto.ClienteDto;
import contacloud.dplicencias.entity.Licencia;
import contacloud.dplicencias.entity.LicenciaDetalle;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

public class StringUtils {

    public static String generarCodigoLicencia(String nombreCliente) {
        String nombreBase = nombreCliente.length() >= 3 ? nombreCliente.substring(0, 3).toUpperCase() : nombreCliente.toUpperCase();
        SecureRandom random = new SecureRandom();
        int numeroAleatorio = random.nextInt(100000);
        return nombreBase + String.format("%05d", numeroAleatorio);
    }

    public static String generarContrasena(String nombreCliente) {
        String base = nombreCliente.substring(0, Math.min(nombreCliente.length(), 4)).toLowerCase();
        SecureRandom random = new SecureRandom();
        StringBuilder contrasena = new StringBuilder(base);
        for (int i = 0; i < 4; i++) {
            contrasena.append((char) (random.nextInt(26) + 'a'));
        }
        return contrasena.toString();
    }


}
