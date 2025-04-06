package com.sip.pdfexcel.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
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
import com.sip.pdfexcel.entities.Facture;
import com.sip.pdfexcel.entities.Product;
import com.sip.pdfexcel.entities.Provider;
import com.sip.pdfexcel.repositories.FactureRepository;
@RestController
@CrossOrigin(origins = "*") // autorise Angular ou autre frontend à accéder
@RequestMapping("/api")
public class InvoiceController {

	
	@Autowired
	private FactureRepository factureRepository;
	
	
	@PostMapping(value = "/invoice/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public void generateInvoice(@RequestBody InvoiceRequestDTO request, HttpServletResponse response) throws IOException {
	    
		// 1. Générer numéro incrémental
        String numero = generateNumeroFacture();
        String nomFichier = "facture-" + numero + ".pdf";
        double totalTtc = request.getProducts().stream()
                .mapToDouble(p -> p.getPrice() * 1.2) // TVA 20%
                .sum();

        // 2. Sauvegarder la facture en BDD
        Facture facture = new Facture(numero, totalTtc, LocalDate.now(), nomFichier);
        factureRepository.save(facture);

        // 3. Enregistrer le fichier PDF sur disque
        String outputPath = "src/main/resources/factures/" + nomFichier;
        try (PdfWriter writer = new PdfWriter(outputPath);
             PdfDocument pdf = new PdfDocument(writer);
             Document doc = new Document(pdf)) {
            generatePdfContent(doc, request, numero, totalTtc);
        }

        // 4. Retourner le fichier au frontend (stream response)
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + nomFichier);

        InputStream inputStream = new FileInputStream(outputPath);
        StreamUtils.copy(inputStream, response.getOutputStream());
    }

    private String generateNumeroFacture() {
        Optional<Facture> last = factureRepository.findTopByOrderByIdDesc();
        int next = last.map(f -> Integer.parseInt(f.getNumero().replace("FAC", "")) + 1).orElse(1);
        return String.format("FAC%04d", next);
    }

    private void generatePdfContent(Document doc, InvoiceRequestDTO request, String numero, double totalTtc) throws IOException {
        // Logo
        InputStream logoStream = getClass().getResourceAsStream("/logo.jpeg");
        if (logoStream != null) {
            byte[] imageBytes = logoStream.readAllBytes();
            ImageData imageData = ImageDataFactory.create(imageBytes);
            Image logo = new Image(imageData).setWidth(100);
            doc.add(logo);
        }

        // Titre
        doc.add(new Paragraph("FACTURE N° " + numero).setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
        doc.add(new Paragraph("Date : " + LocalDate.now()));
        doc.add(new Paragraph("Fournisseur : " + request.getProvider().getLibelle()));
        doc.add(new Paragraph("Adresse : " + request.getProvider().getAddress()));
        doc.add(new Paragraph("Email : " + request.getProvider().getEmail()));
        doc.add(new Paragraph("\n"));

        // Table
        Table table = new Table(UnitValue.createPercentArray(new float[]{4, 2, 2}))
                .useAllAvailableWidth();
        table.addHeaderCell(new Cell().add(new Paragraph("Produit").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("PU (€)").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("TTC (€)").setBold()));

        for (Product p : request.getProducts()) {
            table.addCell(new Paragraph(p.getName()));
            table.addCell(new Paragraph(String.format("%.2f", p.getPrice())));
            table.addCell(new Paragraph(String.format("%.2f", p.getPrice() * 1.2)));
        }

        doc.add(table);

        // Total
        doc.add(new Paragraph("\nTotal TTC : " + String.format("%.2f €", totalTtc)).setBold().setTextAlignment(TextAlignment.RIGHT));
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
	}
	*/
