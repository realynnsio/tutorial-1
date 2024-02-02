package  id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.cache.support.NullValue;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class ProductRepository {
    private List<Product> productData = new ArrayList<>();

    public Product create(Product product) {
        productData.add(product);
        return product;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public void delete(Product product) {
        productData.remove(product);
    }

    public Product findById(String productId) throws ProductNotFoundException {
        for (Product product : productData) {
            if (product.getProductId().equals(productId)) return product;
        }
        throw new ProductNotFoundException("Product not found for id: " + productId);
    }

    class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }
}


