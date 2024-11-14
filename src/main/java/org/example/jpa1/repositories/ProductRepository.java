package org.example.jpa1.repositories;

import org.example.jpa1.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);
    List<Product> findByNameAndPrice(String name, Double price);
    List<Product> findByPriceGreaterThan(double v);
}