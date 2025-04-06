package com.sip.pdfexcel.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Facture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero; // ex: FAC0001
    private double prixTtc;
    private LocalDate date;
    private String nomFacture;
    
    
	public Facture(String numero, double prixTtc, LocalDate date, String nomFacture) {
		super();
		this.numero = numero;
		this.prixTtc = prixTtc;
		this.date = date;
		this.nomFacture = nomFacture;
	}
	
	public Facture() {
	}
	
	@Override
	public String toString() {
		return "Facture [id=" + id + ", numero=" + numero + ", prixTtc=" + prixTtc + ", date=" + date + ", nomFacture="
				+ nomFacture + "]";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public double getPrixTtc() {
		return prixTtc;
	}
	public void setPrixTtc(double prixTtc) {
		this.prixTtc = prixTtc;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getNomFacture() {
		return nomFacture;
	}
	public void setNomFacture(String nomFacture) {
		this.nomFacture = nomFacture;
	}
}
