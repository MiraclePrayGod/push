package contacloud.dplicencias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DpLicenciasApplication {

    public static void main(String[] args) {
        SpringApplication.run(DpLicenciasApplication.class, args);
    }

}
