package com.sip.pdfexcel.dto;

import java.util.List;

import com.sip.pdfexcel.entities.Product;
import com.sip.pdfexcel.entities.Provider;

public class InvoiceRequestDTO {
	private Provider provider;
	private List<Product> products;

	@Override
	public String toString() {
		return "InvoiceRequestDTO [provider=" + provider + ", products=" + products + "]";
	}

	public InvoiceRequestDTO(Provider provider, List<Product> products) {
		super();
		this.provider = provider;
		this.products = products;
	}

	public InvoiceRequestDTO() {

	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

}
