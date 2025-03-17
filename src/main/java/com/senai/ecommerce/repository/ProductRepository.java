package com.senai.ecommerce.repository;

import com.senai.ecommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where p.name = :name")
    public Product findByName(String name);

    @Query("select p from Product p where lower(p.name) like lower(concat('%', :name, '%'))")
    public List<Product> findByNameContainingIgnoreCase(String name);
}
