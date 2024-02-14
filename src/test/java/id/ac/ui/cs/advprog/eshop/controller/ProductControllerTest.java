package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import id.ac.ui.cs.advprog.eshop.service.ProductServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.ui.Model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductServiceImpl productService;

    @InjectMocks
    ProductController productController;

    List<Product> productData = new ArrayList<>();

    Product createProduct(String id, String name, int quantity) {
        Product product = new Product();
        product.setProductId(id);
        product.setProductName(name);
        product.setProductQuantity(quantity);
        return product;
    }

    Product addProductIntoRepository(Product product) {
        productData.add(product);
        return product;
    }

    Product mockFindById(String productId) throws RuntimeException {
        for (Product product : productData) {
            if (product.getProductId().equals(productId)) return product;
        }
        throw new ProductRepository.ProductNotFoundException("Product not found for id: " + productId);
    }

    Product mockEdit(Product product) throws RuntimeException {
        Product changedProduct = mockFindById(product.getProductId());
        changedProduct.setProductQuantity(product.getProductQuantity());
        changedProduct.setProductName(product.getProductName());
        return changedProduct;
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void clearProductData() {
        productData = new ArrayList<>();
    }

    @Test
    void goToHomePageTest() throws Exception {
        mockMvc.perform(get(""))
                .andExpect(status().isOk())
                .andExpect(view().name("homePage"))
                .andExpect(content().string(containsString("Welcome")));
    }

    @Test
    void createProductPageTest() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(content().string(containsString("Create New Product")))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void createProductPostRedirectToProductListTest() throws Exception {
        Product product = createProduct("1000000", "Liesl", 1);

        when(productService.create(product)).thenReturn(addProductIntoRepository(product));
        mockMvc.perform(post("/product/create").flashAttr("product", product))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        when(productService.findAll()).thenReturn(productData);
        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(content().string(containsString("Product List")))
                .andExpect(content().string(containsString(product.getProductName())));

        verify(productService, times(1)).create(product);
        verify(productService, times(1)).findAll();
    }

    @Test
    void productListPageTest() throws Exception {
        Product product = createProduct("1000000", "Liesl", 1);
        Product product2 = createProduct("19", "Mischa", 1);
        addProductIntoRepository(product);
        addProductIntoRepository(product2);

        when(productService.findAll()).thenReturn(productData);

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(content().string(containsString("Product List")))
                .andExpect(model().attribute("products", productData))
                .andExpect(content().string(containsString(product.getProductName())))
                .andExpect(content().string(containsString(product2.getProductName())));

        verify(productService, times(1)).findAll();
    }

    @Test
    void editProductPageTest() throws Exception {
        Product product = createProduct("1000000", "Liesl", 1);
        String productId = product.getProductId();
        addProductIntoRepository(product);

        when(productService.findById(productId)).thenReturn(product);

        mockMvc.perform(get("/product/edit/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(content().string(containsString("Edit Product")))
                .andExpect(model().attribute("product", product));

        // Verify
        verify(productService, times(1)).findById(productId);
    }

    @Test
    void editProductPostRedirectToProductListTest() throws Exception {
        String productId = "1000000";
        Product product = createProduct(productId, "Liesl", 1);
        Product product2 = createProduct(productId, "Mischa", 1);
        addProductIntoRepository(product);

        when(productService.edit(product2)).thenReturn(mockEdit(product2));

        mockMvc.perform(post("/product/edit-product/{productId}", productId).flashAttr("product", product2))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        when(productService.findAll()).thenReturn(productData);
        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(content().string(containsString("Product List")))
                .andExpect(content().string(not(containsString("Liesl"))))
                .andExpect(content().string(containsString("Mischa")));

        verify(productService, times(1)).edit(product2);
        verify(productService, times(1)).findAll();
    }

    @Test
    void testEditProductPageExceptionHandling() {
        String productId = "not-found";
        when(productService.findById(productId)).thenThrow(new RuntimeException("Test exception"));

        Model model = mock(Model.class);

        PrintStream originalErr = System.err;
        PrintStream mockErr = mock(PrintStream.class);
        System.setErr(mockErr);

        try {
            String result = productController.editProductPage(productId, model);

            verify(mockErr).println("Test exception");
            assertEquals("editProduct", result);
        } finally {
            System.setErr(originalErr);
        }
    }

    @Test
    void deleteProductPost_ShouldRedirectToProductList() throws Exception {
        Product product = createProduct("1000000", "Liesl", 1);
        String productId = product.getProductId();
        addProductIntoRepository(product);

        assertEquals(1, productData.size());

        doAnswer(invocation -> {
            productData.remove(product);
            return null; // Void method, so return null
        }).when(productService).delete(productId);

        mockMvc.perform(get("/product/delete/{productId}", productId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        assertEquals(0, productData.size());
        verify(productService, times(1)).delete(productId);
    }
}
