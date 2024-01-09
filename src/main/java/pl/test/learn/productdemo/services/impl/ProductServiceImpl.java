package pl.test.learn.productdemo.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.test.learn.productdemo.exceptions.NoSuchProductException;
import pl.test.learn.productdemo.models.Product;
import pl.test.learn.productdemo.models.dtos.ProductDTO;
import pl.test.learn.productdemo.models.mappers.ProductMapper;
import pl.test.learn.productdemo.repositories.ProductRepository;
import pl.test.learn.productdemo.services.ProductService;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Product createProduct(ProductDTO productDTO) {
        log.info("Creating new product with provided data: %s".formatted(productDTO));
        Product entity = productMapper.toEntity(productDTO);
        return productRepository.save(entity);
    }

    @Override
    public ProductDTO getProductById(Long id) throws NoSuchProductException {
        log.info("Searching for product with id: %s".formatted(id));
        Product product = productRepository.findById(id)
                .orElseThrow(NoSuchProductException::new);
        return productMapper.toDto(product);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        log.info("querying all products");
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateProduct(Long id, ProductDTO productDTO) throws NoSuchProductException {
        log.info("Updating product with provided data: %s".formatted(productDTO));
        Product product = productRepository.findById(id)
                .orElseThrow(NoSuchProductException::new);
        productMapper.partialUpdate(productDTO, product);
        productRepository.save(product);
    }

    @Override
    public void deleteProductById(Long id) throws NoSuchProductException {
        log.info("Deleting product with id: %s".formatted(id));
        Product product = productRepository.findById(id).orElseThrow(NoSuchProductException::new);
        productRepository.delete(product);
    }
}
