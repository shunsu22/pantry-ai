package com.shunsu22.pantryai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PantryAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PantryAiApplication.class, args);
    }

}
