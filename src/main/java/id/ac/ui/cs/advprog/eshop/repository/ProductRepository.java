package  id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
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

    public void delete(String productId) {
        Product deletedProduct = findById(productId);
        productData.remove(deletedProduct);
    }

    public Product findById(String productId) throws ProductNotFoundException {
        for (Product product : productData) {
            if (product.getProductId().equals(productId)) return product;
        }
        throw new ProductNotFoundException("Product not found for id: " + productId);
    }

    public Product edit(Product product) {
        Product changedProduct = findById(product.getProductId());
        changedProduct.setProductQuantity(product.getProductQuantity());
        changedProduct.setProductName(product.getProductName());
        return changedProduct;
    }

    class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }
}


