package pl.test.learn.productdemo.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import pl.test.learn.productdemo.exceptions.NoSuchProductException;
import pl.test.learn.productdemo.models.Product;
import pl.test.learn.productdemo.models.dtos.ProductDTO;
import pl.test.learn.productdemo.models.mappers.ProductMapper;
import pl.test.learn.productdemo.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceImplTest {
    private static final Long QUERY_ID = 1L;
    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Captor
    ArgumentCaptor<Product> productCaptor;

    private ProductDTO productDTO1;

    @BeforeEach
    void setUp() {
        productDTO1 = new ProductDTO();
        productDTO1.setId(QUERY_ID);
        productDTO1.setName("Product 1");
        productDTO1.setDescription("Description for product 1");
        productDTO1.setPrice(12.5);
        productDTO1.setQuantity(5);
    }

    @Test
    public void shouldCreateProductWhenProductDtoIsValid() {
        //given
        Product product = new Product();
        product.setName(productDTO1.getName());
        product.setDescription(productDTO1.getDescription());
        product.setPrice(productDTO1.getPrice());
        product.setQuantity(productDTO1.getQuantity());

        when(productMapper.toEntity(productDTO1)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        //when
        Product result = productService.createProduct(productDTO1);

        //then
        verify(productMapper).toEntity(productDTO1);
        verify(productRepository).save(productCaptor.capture());

        Product savedProduct = productCaptor.getValue();
        assertNotNull(savedProduct);
        assertEquals(productDTO1.getName(), savedProduct.getName());
        assertEquals(productDTO1.getDescription(), savedProduct.getDescription());
        assertEquals(productDTO1.getPrice(), savedProduct.getPrice());
        assertEquals(productDTO1.getQuantity(), savedProduct.getQuantity());
        assertNotNull(result);
        assertEquals(product, result);
    }

    @Test
    void shouldGetProductById() throws NoSuchProductException {
        //given
        Product product = new Product();

        when(productRepository.findById(QUERY_ID)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDTO1);

        //when
        ProductDTO actual = productService.getProductById(QUERY_ID);

        //then
        assertEquals(productDTO1, actual);
    }

    @Test
    void shouldThrowNoSuchProductWhenGetProductById() {
        //given
        when(productRepository.findById(QUERY_ID)).thenReturn(Optional.empty());

        //then
        assertThrows(NoSuchProductException.class, () -> productService.getProductById(QUERY_ID));
    }

    @Test
    public void shouldReturnAllProducts() {
        //given
        List<Product> productsList = new ArrayList<>();
        productsList.add(new Product());
        productsList.add(new Product());
        List<ProductDTO> productsDTOList = new ArrayList<>();
        productsDTOList.add(productDTO1);

        when(productRepository.findAll()).thenReturn(productsList);
        when(productMapper.toDto(Mockito.any(Product.class))).thenReturn(productDTO1);

        //then
        assertEquals(2, productService.getAllProducts().size());
    }

    @Test
    public void shouldUpdateProductWhenProductExists() throws NoSuchProductException {
        // given
        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // when
        productService.updateProduct(1L, productDTO1);

        // then
        verify(productRepository).findById(any(Long.class));
        verify(productRepository).save(any(Product.class));
        verify(productMapper).partialUpdate(productDTO1, product);
    }

    @Test
    public void shouldThrowNoSuchProductExceptionWhenProductDoesNotExist() {
        // given
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // then
        org.junit.jupiter.api.Assertions.assertThrows(
                NoSuchProductException.class,
                () -> productService.updateProduct(any(),productDTO1)
        );
    }

    @Test
    public void shouldDeleteProductWhenProductExists() throws NoSuchProductException {
        //given
        Product product = new Product();
        when(productRepository.findById(QUERY_ID)).thenReturn(Optional.of(product));

        //when
        productService.deleteProductById(QUERY_ID);

        //then
        verify(productRepository, times(1)).delete(product);
        assertDoesNotThrow(() -> productService.deleteProductById(QUERY_ID));
    }

    @Test
    public void shouldThrowNoSuchProductWhenProductDoesNotExist() {
        //given
        when(productRepository.findById(QUERY_ID)).thenReturn(Optional.empty());
        //then
        assertThrows(NoSuchProductException.class, () -> productService.deleteProductById(QUERY_ID));
    }

}