package com.pedromolon.CasaAndrade.repository;

import com.pedromolon.CasaAndrade.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByDocument(String document);
    Page<Client> findByActiveTrue(Pageable pageable);
}
