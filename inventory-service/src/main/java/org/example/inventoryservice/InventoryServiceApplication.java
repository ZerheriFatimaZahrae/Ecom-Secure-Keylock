package org.example.inventoryservice;

import org.example.inventoryservice.entities.Product;
import org.example.inventoryservice.repo.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }


    @Bean
    CommandLineRunner start(ProductRepository productRepository) {
        return args -> {
            Random random = new Random();

            for (int i = 1; i < 10; i++) {
                Product product = Product.builder()
                        .id(UUID.randomUUID().toString()) // Génère un ID unique
                        .name("Computer " + i)            // Définit le nom
                        .price(1200 + Math.random() * 10000) // Génère un prix aléatoire
                        .quantity(1 + random.nextInt(200))  // Définit une quantité aléatoire
                        .build();

                // Sauvegarde le produit dans le repository
                productRepository.save(product);
            }
        };
    }

}
