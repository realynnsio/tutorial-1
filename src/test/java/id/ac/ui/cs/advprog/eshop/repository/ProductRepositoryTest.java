package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
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
        productRepository.create(product);
        return product;
    }
    @Test
    void testCreateAndFind() {
        Product product = createProductInRepository("eb558e9f-1c39-460e-8860-71af6af63bd6", "Sampo Cap Bambang", 100);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = createProductInRepository("eb558e9f-1c39-460e-8860-71af6af63bd6", "Sampo Cap Bambang", 100);
        Product product2 = createProductInRepository("a0f9de46-90b1-437d-a0bf-d0821dde9096", "Sampo Cap Usep", 50);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testDeleteIfExist() {
        Product product = createProductInRepository("eb558e9f-1c39-460e-8860-71af6af63bd6", "Sampo Cap Bambang", 100);
        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());

        productRepository.delete(product.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testDeleteIfNotExist() {
        Product product = createProductInRepository("eb558e9f-1c39-460e-8860-71af6af63bd6", "Sampo Cap Bambang", 100);
        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());

        assertThrows(RuntimeException.class, () -> productRepository.delete("0"));
    }

    @Test
    void testEditIfExist() {
        Product product1 = createProductInRepository("eb558e9f-1c39-460e-8860-71af6af63bd6", "Sampo Cap Bambang", 100);
        Product product2 = createProduct(product1.getProductId(), "New Name", 33);

        productRepository.edit(product2);
        assertEquals(product1.getProductId(), product2.getProductId());
        assertEquals(product1.getProductName(), product2.getProductName());
        assertEquals(product1.getProductQuantity(), product2.getProductQuantity());
    }

    @Test
    void testEditIfNotExist() {
        Product product1 = createProductInRepository("eb558e9f-1c39-460e-8860-71af6af63bd6", "Sampo Cap Bambang", 100);
        Product product2 = createProduct("0", "New Name", 33);

        assertThrows(RuntimeException.class, () -> productRepository.edit(product2));
    }

}