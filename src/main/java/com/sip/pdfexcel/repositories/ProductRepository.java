package com.sip.pdfexcel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sip.pdfexcel.entities.Product;

public interface ProductRepository extends JpaRepository<Product,Long>{

}
