package pl.test.learn.productdemo.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.test.learn.productdemo.exceptions.NoSuchProductException;
import pl.test.learn.productdemo.models.dtos.ProductDTO;
import pl.test.learn.productdemo.services.ProductService;

import java.util.List;

@RestController
@Slf4j
public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") Long id) {
        try {
            ProductDTO productById = productService.getProductById(id);
            return ResponseEntity.ok(productById);

        } catch (NoSuchProductException e) {
            log.error("There has been an error while querying for entity. Exception is: %s".formatted(e.getMessage()));
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/products")
    public ResponseEntity<String> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        try {
            productService.createProduct(productDTO);
            return ResponseEntity.ok("Created entity from data: %s".formatted(productDTO.toString()));
        } catch (Exception e) {
            log.error("There has been an error while querying for entity. Exception is: %s".formatted(e.getMessage()));
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<String> updateProductById(@PathVariable("id") Long id,
                                                    @Valid @RequestBody ProductDTO productDTO) {
        try {
            productService.updateProduct(id, productDTO);
            return ResponseEntity.ok("Entity updated with data: id=%s; %s".formatted(id, productDTO));
        } catch (NoSuchProductException e) {
            log.error("There has been an error while querying for entity. Exception is: %s".formatted(e.getMessage()));
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable("id") Long id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok("Entity deleted with id: %s".formatted(id));
        } catch (NoSuchProductException e) {
            log.error("There has been an error while querying for entity. Exception is: %s".formatted(e.getMessage()));
            return ResponseEntity.badRequest().build();
        }
    }
}
