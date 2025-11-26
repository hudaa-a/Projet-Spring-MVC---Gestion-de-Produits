package ma.enset.spring_mvc;

import ma.enset.spring_mvc.entities.Product;
import ma.enset.spring_mvc.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication// Annotation principale qui marque cette classe comme application Spring Boot
// @SpringBootApplication combine trois annotations importantes:
// - @Configuration: Déclare cette classe comme source de configuration Spring
// - @EnableAutoConfiguration: Active la configuration automatique de Spring Boot
// - @ComponentScan: Scanne les composants dans le package courant et ses sous-packages
public class SpringMvcApplication {
    // Méthode principale - point d'entrée de l'application
    public static void main(String[] args) {
        // Lance l'application Spring Boot
        // Cette méthode:
        // 1. Crée le contexte Spring (container IoC)
        // 2. Démarre le serveur embarqué (Tomcat)
        // 3. Déploie l'application
        SpringApplication.run(SpringMvcApplication.class, args);
    }
    @Bean
    public CommandLineRunner start(ProductRepository productRepository){
        // Retourne une implémentation de CommandLineRunner (utilise une lambda expression)
        return args -> {// args représente les arguments de ligne de commande
            productRepository.save(Product.builder()
                            .name("computer")
                            .price(3300)
                            .quantity(30)
                    .build());
            productRepository.save(Product.builder()
                    .name("phone")
                    .price(7200)
                    .quantity(70)
                    .build());
            productRepository.save(Product.builder()
                    .name("printer")
                    .price(2000)
                    .quantity(17)
                    .build());
            productRepository.findAll().forEach(p ->
            {
                System.out.println(
                        p.toString()
                );
            });

        };

    }

}
