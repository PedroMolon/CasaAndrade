package com.pedromolon.CasaAndrade.model;

import com.pedromolon.CasaAndrade.model.enums.PersonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_client")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "persons_type", length = 10, nullable = false)
    private PersonType personType;

    @Column(length = 150, nullable = false)
    private String name;

    @Column(length = 20, nullable = false, unique = true)
    private String document;

    @Column(length = 150)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "client")
    private List<Sale> sales = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
