package com.example.springboot.repositories;

import com.example.springboot.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductModel, Long> {

    Optional<ProductModel> findByUuid(UUID uuid);
    boolean existsByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);
}
