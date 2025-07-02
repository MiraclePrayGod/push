package contacloud.msreportes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "contacloud.msreportes.feign") // Asegúrate que el paquete es correcto
public class MsReportesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsReportesApplication.class, args);
    }

}
