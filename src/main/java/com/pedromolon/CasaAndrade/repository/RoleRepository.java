package com.pedromolon.CasaAndrade.repository;

import com.pedromolon.CasaAndrade.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
