package com.sip.pdfexcel.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sip.pdfexcel.entities.Facture;
public interface FactureRepository extends JpaRepository<Facture, Long> {

    // Récupère la dernière facture triée par id descendant
    Optional<Facture> findTopByOrderByIdDesc();
}