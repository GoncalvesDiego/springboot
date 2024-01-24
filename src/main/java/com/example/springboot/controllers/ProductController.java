package com.example.springboot.controllers;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        ProductModel savedProduct = productService.saveProduct(productRecordDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ProductModel>>> getAllProducts() {
        List<ProductModel> products = productService.getAllProducts();

        List<EntityModel<ProductModel>> productEntities = products.stream()
                .map(product -> EntityModel.of(product,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductController.class)
                                        .getProductByUuid(product.getUuid()))
                                .withSelfRel()))
                .collect(Collectors.toList());

        Link link = WebMvcLinkBuilder.linkTo(ProductController.class).withSelfRel();

        CollectionModel<EntityModel<ProductModel>> collectionModel = CollectionModel.of(productEntities, link);

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<EntityModel<ProductModel>> getProductByUuid(@PathVariable UUID uuid) {
        Optional<ProductModel> optionalProduct = productService.getProductByUuid(uuid);

        return optionalProduct.map(product -> {
            EntityModel<ProductModel> productEntity = EntityModel.of(product,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductController.class)
                                    .getProductByUuid(product.getUuid()))
                            .withSelfRel());

            return ResponseEntity.ok(productEntity);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ProductModel> updateProduct(@PathVariable UUID uuid, @RequestBody @Valid ProductRecordDto productRecordDto) {
        Optional<ProductModel> optionalUpdatedProduct = productService.updateProduct(uuid, productRecordDto);
        return optionalUpdatedProduct.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID uuid) {
        boolean deleted = productService.deleteProduct(uuid);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
