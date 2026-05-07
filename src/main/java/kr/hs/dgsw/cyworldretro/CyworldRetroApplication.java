package kr.hs.dgsw.cyworldretro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CyworldRetroApplication {

    public static void main(String[] args) {
        SpringApplication.run(CyworldRetroApplication.class, args);
    }

}
