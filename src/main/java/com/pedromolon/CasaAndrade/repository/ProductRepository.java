package com.pedromolon.CasaAndrade.repository;

import com.pedromolon.CasaAndrade.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByActiveTrue(Pageable pageable);

    @Query("SELECT p FROM Product WHERE p.quantity <= p.min_quantity AND p.active = true")
    List<Product> findLowQuantity();

}
