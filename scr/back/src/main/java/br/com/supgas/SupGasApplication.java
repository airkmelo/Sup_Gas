package br.com.supgas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Ponto de entrada. Deve ficar na RAIZ do pacote (br.com.supgas):
// o Spring procura @Entity, @Service, @RestController... daqui para baixo.
@SpringBootApplication
public class SupGasApplication {
    public static void main(String[] args) {
        SpringApplication.run(SupGasApplication.class, args);
    }
}