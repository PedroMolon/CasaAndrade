package com.pedromolon.CasaAndrade.repository;

import com.pedromolon.CasaAndrade.model.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    Page<Sale> findByClientId(Long clientId, Pageable pageable);
    Page<Sale> findBySellerId(Long sellerId, Pageable pageable);
}
