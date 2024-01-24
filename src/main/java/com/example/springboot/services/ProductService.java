package com.example.springboot.services;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductModel saveProduct(ProductRecordDto productRecordDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return productRepository.save(productModel);
    }

    public List<ProductModel> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<ProductModel> getProductByUuid(UUID uuid) {
        return productRepository.findByUuid(uuid);
    }

    public Optional<ProductModel> updateProduct(UUID uuid, ProductRecordDto productRecordDto) {
        Optional<ProductModel> optionalProduct = productRepository.findByUuid(uuid);
        if (optionalProduct.isPresent()) {
            ProductModel existingProduct = optionalProduct.get();
            BeanUtils.copyProperties(productRecordDto, existingProduct);
            return Optional.of(productRepository.save(existingProduct));
        } else {
            return Optional.empty();
        }
    }
@Transactional
    public boolean deleteProduct(UUID uuid) {
        if (productRepository.existsByUuid(uuid)) {
            productRepository.deleteByUuid(uuid);
            return true;
        } else {
            return false;
        }
    }
}
