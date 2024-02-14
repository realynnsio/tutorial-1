package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productService;

    private List<Product> productData = new ArrayList<>();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    Product createProduct(String id, String name, int quantity) {
        Product product = new Product();
        product.setProductId(id);
        product.setProductName(name);
        product.setProductQuantity(quantity);
        return product;
    }

    Product createProductInRepository(String id, String name, int quantity) {
        Product product = createProduct(id, name, quantity);
        productData.add(product);
        return product;
    }

    Product mockFindById(String productId) throws RuntimeException {
        for (Product product : productData) {
            if (product.getProductId().equals(productId)) return product;
        }
        throw new ProductNotFoundException("Product not found for id: " + productId);
    }

    Product mockEdit(Product product) throws RuntimeException {
        Product changedProduct = mockFindById(product.getProductId());
        changedProduct.setProductQuantity(product.getProductQuantity());
        changedProduct.setProductName(product.getProductName());
        return changedProduct;
    }

    @Test
    void testCreateProduct() {
        // Exercise
        Product product = createProduct("eb558e9f-1c39-460e-8860-71af6af63bd6", "Sampo Cap Bambang", 100);
        when(productRepository.create(product)).thenReturn(product);
        Product createdProduct = productService.create(product);

        // Assert
        assertNotNull(createdProduct);
        assertEquals(product, createdProduct);

        // Verify
        verify(productRepository, times(1)).create(product);
    }

    @Test
    void testCreateAndFind() {
        // Exercise
        Product product = createProductInRepository("eb558e9f-1c39-460e-8860-71af6af63bd6", "Sampo Cap Bambang", 100);
        when(productRepository.findAll()).thenReturn(productData.iterator());
        List<Product> allProducts = productService.findAll();
        Iterator<Product> productIterator = allProducts.iterator();

        // Assert
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());

        // Verify
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindAll() {
        // Exercise
        Product product = createProductInRepository("eb558e9f-1c39-460e-8860-71af6af63bd6", "Sampo Cap Bambang", 100);
        Product product2 = createProductInRepository("1c39-460e-8860-71af6af63bd6", "Sampo Cap Budi", 10);
        Product product3 = createProductInRepository("460e-8860-71af6af63bd6", "Sampo Cap Becky", 1);
        when(productRepository.findAll()).thenReturn(productData.iterator());
        List<Product> allProducts = productService.findAll();

        // Assert
        assertNotNull(allProducts);
        assertEquals(productData, allProducts);

        // Verify
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindAllWhenEmpty() {
        // Exercise
        when(productRepository.findAll()).thenReturn(productData.iterator());
        List<Product> allProducts = productService.findAll();

        // Assert
        assertFalse(allProducts.iterator().hasNext());
        assertEquals(productData, allProducts);

        // Verify
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdIfExist() {
        // Exercise
        Product product = createProductInRepository("eb558e9f-1c39-460e-8860-71af6af63bd6", "Sampo Cap Bambang", 100);
        String productId = product.getProductId();
        when(productRepository.findById(productId)).thenReturn(mockFindById(productId));
        Product foundProduct = productService.findById(product.getProductId());

        // Assert
        assertNotNull(foundProduct);
        assertEquals(foundProduct.getProductId(), product.getProductId());
        assertEquals(foundProduct.getProductName(), product.getProductName());
        assertEquals(foundProduct.getProductQuantity(), product.getProductQuantity());

        // Verify
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testFindByIdIfNotExist() {
        // Exercise
        Product product = createProductInRepository("eb558e9f-1c39-460e-8860-71af6af63bd6", "Sampo Cap Bambang", 100);
        String productId = "not-found";
        when(productRepository.findById(productId)).thenThrow(new ProductNotFoundException("Product not found for id: " + productId));

        // Assert
        assertThrows(RuntimeException.class, () -> productService.findById(productId));

        // Verify
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testDeleteIfExist() {
        // Exercise
        Product product = createProductInRepository("eb558e9f-1c39-460e-8860-71af6af63bd6", "Sampo Cap Bambang", 100);
        doAnswer(invocation -> {
            productData.remove(product);
            return null; // Void method, so return null
        }).when(productRepository).delete(product.getProductId());

        when(productRepository.findAll()).thenReturn(productData.iterator());
        List<Product> allProducts = productService.findAll();

        // Assert
        assertTrue(allProducts.iterator().hasNext());
        productService.delete(product.getProductId());

        // Refresh the allProducts list after delete
        allProducts = productService.findAll();
        assertFalse(allProducts.iterator().hasNext());

        // Verify
        verify(productRepository, times(1)).delete(product.getProductId());
    }

    @Test
    void testDeleteIfNotExist() {
        // Exercise
        Product product = createProductInRepository("eb558e9f-1c39-460e-8860-71af6af63bd6", "Sampo Cap Bambang", 100);
        String productId = "not-found";
        doAnswer(invocation -> {
            throw new ProductNotFoundException("Product not found for id: " + productId);
        }).when(productRepository).delete(product.getProductId());
        when(productRepository.findAll()).thenReturn(productData.iterator());
        List<Product> allProducts = productService.findAll();

        // Assert
        assertTrue(allProducts.iterator().hasNext());
        assertThrows(RuntimeException.class, () -> productService.delete(productId));

        // Verify
        verify(productRepository, times(1)).delete(productId);
    }

    @Test
    void testEditIfExist() {
        Product product1 = createProductInRepository("eb558e9f-1c39-460e-8860-71af6af63bd6", "Sampo Cap Bambang", 100);
        Product product2 = createProduct(product1.getProductId(), "New Name", 33);

        when(productRepository.edit(product2)).thenReturn(mockEdit(product2));
        productService.edit(product2);

        // Assert
        assertEquals(product1.getProductId(), product2.getProductId());
        assertEquals(product1.getProductName(), product2.getProductName());
        assertEquals(product1.getProductQuantity(), product2.getProductQuantity());

        // Verify
        verify(productRepository, times(1)).edit(product2);
    }

    @Test
    void testEditIfNotExist() {
        // Exercise
        Product product1 = createProductInRepository("eb558e9f-1c39-460e-8860-71af6af63bd6", "Sampo Cap Bambang", 100);
        Product product2 = createProduct("0", "New Name", 33);
        String productId = product2.getProductId();

        when(productRepository.edit(product2)).thenThrow(new ProductNotFoundException("Product not found for id: " + productId));

        // Assert
        assertThrows(RuntimeException.class, () -> productService.edit(product2));

        // Verify
        verify(productRepository, times(1)).edit(product2);
    }
}
