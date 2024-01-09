package pl.test.learn.productdemo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.test.learn.productdemo.exceptions.NoSuchProductException;
import pl.test.learn.productdemo.models.Product;
import pl.test.learn.productdemo.models.dtos.ProductDTO;
import pl.test.learn.productdemo.services.ProductService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@WithMockUser
class ProductControllerTest {

    private static final String URL = "/products";
    private static final String URL_ID = "/products/{id}";

    private static final Long QUERY_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO productDTO1;
    private ProductDTO productDTO2;

    @BeforeEach
    void setUp() {
        productDTO1 = new ProductDTO();
        productDTO1.setId(1L);
        productDTO1.setName("Product 1");
        productDTO1.setDescription("Description for product 1");
        productDTO1.setPrice(12.5);
        productDTO1.setQuantity(5);

        productDTO2 = new ProductDTO();
        productDTO2.setId(2L);
        productDTO2.setName("Product 2");
        productDTO2.setDescription("Description for product 2");
        productDTO2.setPrice(10.5);
        productDTO2.setQuantity(7);
    }

    @Test
    public void shouldGetAllProducts() throws Exception {
        //given
        List<ProductDTO> products = Arrays.asList(productDTO1, productDTO2);
        Mockito.when(productService.getAllProducts()).thenReturn(products);

        // when // then
        mockMvc.perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(productDTO1.getId()))
                .andExpect(jsonPath("$[1].id").value(productDTO2.getId()));
    }

    @Test
    void shouldReturnProduct_whenGetProductById_givenValidId() throws Exception, NoSuchProductException {
        //given
        Mockito.when(productService.getProductById(QUERY_ID)).thenReturn(productDTO1);

        // when // then
        mockMvc.perform(get(URL_ID, QUERY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnProductNotFound_whenGetProductById_givenInvalidId() throws NoSuchProductException, Exception {
        //given
        Mockito.when(productService.getProductById(QUERY_ID))
                .thenThrow(new NoSuchProductException());

        // when // then
        mockMvc.perform(get(URL_ID, QUERY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createProduct_ShouldReturnStatusOk() throws Exception {
        //given
        Product entity = new Product();
        entity.setId(QUERY_ID);
        entity.setName("product");
        entity.setDescription("description");
        entity.setPrice(100.0);
        entity.setQuantity(50);

        when(productService.createProduct(productDTO1)).thenReturn(entity);
        String body = objectMapper.writeValueAsString(productDTO1);

        // when // then
        mockMvc.perform(post(URL)
                        .with(csrf().asHeader())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateProduct() throws Exception, NoSuchProductException {
        //given // when // then
        mockMvc.perform(put(URL_ID, QUERY_ID)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO1)))
                .andExpect(status().isOk());

        verify(productService, times(1)).updateProduct(anyLong(), any(ProductDTO.class));
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingInvalidProduct() throws Exception {
        //given
        ProductDTO invalidProduct = new ProductDTO();

        // when // then
        mockMvc.perform(put(URL_ID, QUERY_ID)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProduct)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldDeleteProduct() throws Exception, NoSuchProductException {
        //given // when // then
        mockMvc.perform(delete(URL_ID, QUERY_ID)
                        .with(csrf().asHeader()))
                .andExpect(status().isOk());

        verify(productService, times(1)).deleteProductById(anyLong());
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void shouldThrowExceptionWhenDeletingInvalidProduct() throws Exception, NoSuchProductException {
        //given // when // then
        doThrow(NoSuchProductException.class).when(productService).deleteProductById(anyLong());

        mockMvc.perform(delete(URL_ID, QUERY_ID).with(csrf().asHeader()))
                .andExpect(status().isBadRequest());
    }
}