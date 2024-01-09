package pl.test.learn.productdemo.services;

import pl.test.learn.productdemo.models.Product;
import pl.test.learn.productdemo.models.dtos.ProductDTO;
import pl.test.learn.productdemo.exceptions.NoSuchProductException;

import java.util.List;

public interface ProductService {

    Product createProduct(ProductDTO productDTO);

    ProductDTO getProductById(Long id) throws NoSuchProductException;

    List<ProductDTO> getAllProducts();

    void updateProduct(Long id, ProductDTO productDTO) throws NoSuchProductException;

    void deleteProductById(Long id) throws NoSuchProductException;
}
