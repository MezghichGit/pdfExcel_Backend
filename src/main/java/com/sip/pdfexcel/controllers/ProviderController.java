package com.sip.pdfexcel.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sip.pdfexcel.repositories.ProviderRepository;
import com.sip.pdfexcel.entities.Provider;
@RestController
@RequestMapping("/api/providers")
@CrossOrigin(origins = "*") // autorise Angular ou autre frontend à accéder
public class ProviderController {

    private final ProviderRepository providerRepository;

    public ProviderController(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @GetMapping
    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Provider> getProviderById(@PathVariable Long id) {
        return providerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
