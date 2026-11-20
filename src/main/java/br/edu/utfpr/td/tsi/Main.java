package br.edu.utfpr.td.tsi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.edu.utfpr.td.tsi.service.MedicamentoService;

@SpringBootApplication
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(MedicamentoService service) {
        return args -> {
            if (service.contar() == 0) {
                service.importarArquivo("DADOS_ABERTOS_MEDICAMENTOS.csv");
            }
        };
    }

}