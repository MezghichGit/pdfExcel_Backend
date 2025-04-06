package com.sip.pdfexcel;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sip.pdfexcel.entities.Product;
import com.sip.pdfexcel.entities.Provider;
import com.sip.pdfexcel.repositories.ProductRepository;
import com.sip.pdfexcel.repositories.ProviderRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ProviderRepository providerRepository;
    private final ProductRepository productRepository;

    public DataSeeder(ProviderRepository providerRepository, ProductRepository productRepository) {
        this.providerRepository = providerRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (providerRepository.count() == 0 && productRepository.count() == 0) {
            // Création de fournisseurs
            Provider p1 = new Provider("Fournisseur Alpha", "123 Rue Paris", "alpha@exemple.com");
            Provider p2 = new Provider("Fournisseur Beta", "456 Avenue Lyon", "beta@exemple.com");

            providerRepository.saveAll(List.of(p1, p2));

            // Création de produits liés aux fournisseurs
            Product prod1 = new Product("Produit A", 25.0, p1);
            Product prod2 = new Product("Produit B", 42.5, p1);
            Product prod3 = new Product("Produit C", 10.0, p2);
            Product prod4 = new Product("Produit D", 19.99, p2);

            productRepository.saveAll(List.of(prod1, prod2, prod3, prod4));

            System.out.println("✅ Données initiales insérées !");
        }
    }
}
