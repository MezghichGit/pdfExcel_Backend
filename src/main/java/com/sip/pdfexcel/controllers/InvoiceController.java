package com.sip.pdfexcel.controllers;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.sip.pdfexcel.dto.InvoiceRequestDTO;

import jakarta.servlet.http.HttpServletResponse;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;


import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.sip.pdfexcel.entities.Product;
import com.sip.pdfexcel.entities.Provider;
@RestController
@CrossOrigin(origins = "*") // autorise Angular ou autre frontend à accéder
@RequestMapping("/api")
public class InvoiceController {

	
	@PostMapping(value = "/invoice/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public void generateInvoice(@RequestBody InvoiceRequestDTO request, HttpServletResponse response) throws IOException {
	    response.setContentType("application/pdf");
	    response.setHeader("Content-Disposition", "attachment; filename=invoice.pdf");

	    try (PdfWriter writer = new PdfWriter(response.getOutputStream());
	         PdfDocument pdf = new PdfDocument(writer);
	         Document doc = new Document(pdf)) {

	        // 👉 Ajout du logo
	        InputStream logoStream = getClass().getResourceAsStream("/logo.jpeg");
	        if (logoStream != null) {
	            byte[] imageBytes = logoStream.readAllBytes();
	            ImageData imageData = ImageDataFactory.create(imageBytes);
	            Image logo = new Image(imageData);
	            logo.setWidth(100);
	            logo.setHorizontalAlignment(HorizontalAlignment.LEFT);
	            doc.add(logo);
	        }

	        // 👉 Titre
	        Paragraph title = new Paragraph("FACTURE")
	                .setBold()
	                .setFontSize(20)
	                .setTextAlignment(TextAlignment.CENTER)
	                .setMarginBottom(20);
	        doc.add(title);

	        // 👉 Infos fournisseur
	        Provider provider = request.getProvider();
	        doc.add(new Paragraph("Fournisseur : " + provider.getLibelle()));
	        doc.add(new Paragraph("Email : " + provider.getEmail()));
	        doc.add(new Paragraph("Adresse : " + provider.getAddress()));
	        doc.add(new Paragraph(" "));

	        // 👉 Table des produits
	        Table table = new Table(UnitValue.createPercentArray(new float[]{4, 2, 2}))
	                .useAllAvailableWidth()
	                .setMarginTop(10)
	                .setBorder(Border.NO_BORDER);

	        table.addHeaderCell(new Cell().add(new Paragraph("Produit")).setBold());
	        table.addHeaderCell(new Cell().add(new Paragraph("Prix Unitaire")).setBold());
	        table.addHeaderCell(new Cell().add(new Paragraph("Prix TTC")).setBold());

	        double total = 0;
	        for (Product p : request.getProducts()) {
	            table.addCell(p.getName());
	            table.addCell(String.format("%.2f €", p.getPrice()));
	            double priceTTC = p.getPrice() * 1.2; // TVA 20%
	            total += priceTTC;
	            table.addCell(String.format("%.2f €", priceTTC));
	        }

	        doc.add(table);

	        // 👉 Total général
	        doc.add(new Paragraph(" "));
	        Paragraph totalPara = new Paragraph("Total TTC : " + String.format("%.2f €", total))
	                .setTextAlignment(TextAlignment.RIGHT)
	                .setBold()
	                .setFontSize(14)
	                .setBorderTop(new SolidBorder(1))
	                .setMarginTop(10);
	        doc.add(totalPara);

	        // 👉 Mention TVA
	        doc.add(new Paragraph("Les prix incluent une TVA de 20%").setFontSize(10).setItalic());

	        // 👉 Pied de page simple
	        doc.add(new Paragraph("\nMerci pour votre confiance.")
	                .setTextAlignment(TextAlignment.CENTER)
	                .setFontSize(11)
	                .setMarginTop(20));
	    }
	}

	
	/*
	@PostMapping(value = "/invoice/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public void generateInvoice(@RequestBody InvoiceRequestDTO request, HttpServletResponse response) throws IOException {
	    response.setContentType("application/pdf");
	    response.setHeader("Content-Disposition", "attachment; filename=invoice.pdf");

	    try (PdfWriter writer = new PdfWriter(response.getOutputStream());
	         PdfDocument pdf = new PdfDocument(writer);
	         Document doc = new Document(pdf)) {

	    	// Charger l'image du logo depuis les resources
	    	InputStream logoStream = getClass().getResourceAsStream("/logo.jpeg");

	    	if (logoStream != null) {
	    	    byte[] imageBytes = logoStream.readAllBytes();
	    	    ImageData imageData = ImageDataFactory.create(imageBytes);
	    	    Image logo = new Image(imageData);
	    	    logo.setWidth(100); // redimensionner si besoin
	    	    logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
	    	    doc.add(logo);
	    	} else {
	    	    System.out.println("⚠️ Logo non trouvé dans les resources !");
	    	}
	    	// Charger l'image du logo depuis les resources
	        doc.add(new Paragraph("Facture"));
	        doc.add(new Paragraph("Fournisseur: " + request.getProvider().getLibelle()));
	        doc.add(new Paragraph("Email: " + request.getProvider().getEmail()));
	        doc.add(new Paragraph("Adresse: " + request.getProvider().getAddress()));
	        doc.add(new Paragraph(" "));

	        Table table = new Table(new float[]{4, 2});
	        table.addHeaderCell("Produit");
	        table.addHeaderCell("Prix");

	        for (Product p : request.getProducts()) {
	            table.addCell(p.getName());
	            table.addCell(String.valueOf(p.getPrice()));
	        }

	        doc.add(table);
	    }
	}*/
	
	

}
